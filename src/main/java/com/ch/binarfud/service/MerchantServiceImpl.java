package com.ch.binarfud.service;

import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ch.binarfud.dto.merchant.request.ApprovalMerchantDto;
import com.ch.binarfud.dto.merchant.request.CreateMerchantDto;
import com.ch.binarfud.dto.merchant.request.OpenMerchantDto;
import com.ch.binarfud.dto.merchant.request.RegisterMerchantDto;
import com.ch.binarfud.dto.merchant.request.UpdateMerchantDto;
import com.ch.binarfud.dto.merchant.response.MerchantResponseDto;
import com.ch.binarfud.dto.merchant.response.OpenMerchantResponseDto;
import com.ch.binarfud.dto.merchant.response.RequestMerchantResponseDto;
import com.ch.binarfud.exception.DataNotFoundException;
import com.ch.binarfud.model.Merchant;
import com.ch.binarfud.model.Role;
import com.ch.binarfud.model.User;
import com.ch.binarfud.model.UserIdentity;
import com.ch.binarfud.repository.MerchantRepository;
import com.ch.binarfud.repository.UserIdentityRepository;
import com.ch.binarfud.repository.UserRepository;

@Service
public class MerchantServiceImpl implements MerchantService {

    private final ModelMapper modelMapper;

    private final MerchantRepository merchantRepository;

    private final UserRepository userRepository;

    private final UserIdentityRepository userIdentityRepository;

    public MerchantServiceImpl(ModelMapper modelMapper, MerchantRepository merchantRepository,
            UserRepository userRepository,
            UserIdentityRepository userIdentityRepository) {
        this.modelMapper = modelMapper;
        this.merchantRepository = merchantRepository;
        this.userRepository = userRepository;
        this.userIdentityRepository = userIdentityRepository;
    }

    @Override
    public Page<MerchantResponseDto> getAllMerchant(int page, int size) {
        Page<Merchant> merchants = merchantRepository.findAll(PageRequest.of(page, size));
        return merchants.map(merchant -> modelMapper.map(merchant, MerchantResponseDto.class));
    }

    @Override
    public MerchantResponseDto getMerchantById(UUID id) {
        Merchant existingMerchant = merchantRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Merchant not found"));
        return modelMapper.map(existingMerchant, MerchantResponseDto.class);
    }

    @Override
    public MerchantResponseDto addMerchant(User user, CreateMerchantDto createMerchantDto) {
        if (user.getMerchant() != null) {
            throw new IllegalStateException("User already has a Merchant.");
        }

        Merchant merchant = modelMapper.map(createMerchantDto, Merchant.class);
        merchant.setUser(user);
        Merchant savedMerchant = merchantRepository.save(merchant);

        return modelMapper.map(savedMerchant, MerchantResponseDto.class);
    }

    @Override
    public MerchantResponseDto updateMerchant(User user, UpdateMerchantDto updateMerchantDto) {
        Merchant existingMerchant = Optional.ofNullable(user.getMerchant())
                .orElseThrow(() -> new DataNotFoundException("Merchant not found"));

        modelMapper.map(updateMerchantDto, existingMerchant);
        Merchant savedMerchant = merchantRepository.save(existingMerchant);

        return modelMapper.map(savedMerchant, MerchantResponseDto.class);
    }

    @Override
    public OpenMerchantResponseDto openMerchant(User user, OpenMerchantDto openMerchantDto) {
        Merchant existingMerchant = Optional.ofNullable(user.getMerchant())
                .orElseThrow(() -> new DataNotFoundException("Merchant not found"));

        modelMapper.map(openMerchantDto, existingMerchant);
        Merchant savedMerchant = merchantRepository.save(existingMerchant);

        return modelMapper.map(savedMerchant, OpenMerchantResponseDto.class);
    }

    @Override
    public String registerMerchant(User user, RegisterMerchantDto registerMerchant) {
        if (user.getRoles().contains(Role.Roles.MERCHANT)) {
            return "User is already a merchant.";
        } else if (user.getUserIdentity() != null) {
            if (!user.getUserIdentity().isVerified()) {
                return "Request to become a merchant is still pending.";
            } else {
                user.getRoles().add(Role.Roles.MERCHANT);
                userRepository.save(user);
                return "Request to become a merchant has been approved.";
            }
        } else {
            UserIdentity userIdentity = new UserIdentity();
            userIdentity.setVerified(false);
            userIdentity.setUser(user);
            userIdentity.setFullName(registerMerchant.getFullName());
            userIdentity.setIdNumber(registerMerchant.getIdNumber());

            userIdentityRepository.save(userIdentity);
            return "Request to become a merchant has been sent.";
        }
    }

    @Override
    public Page<RequestMerchantResponseDto> getRequestMerchant(int page, int size) {
        Page<UserIdentity> userIdentities = userIdentityRepository.findByVerified(false, PageRequest.of(page, size));
        return userIdentities.map(userIdentity -> modelMapper.map(userIdentity, RequestMerchantResponseDto.class));
    }

    @Override
    public RequestMerchantResponseDto getRequestMerchantById(UUID id) {
        UserIdentity userIdentity = userIdentityRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Request not found"));
        return modelMapper.map(userIdentity, RequestMerchantResponseDto.class);
    }

    @Override
    public String approvalRequestMerchant(UUID id, ApprovalMerchantDto approvalMerchantDto) {
        UserIdentity userIdentity = userIdentityRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Request not found"));

        boolean isApproved = approvalMerchantDto.isApproval();

        if (isApproved) {
            userIdentity.setVerified(isApproved);
            userIdentityRepository.save(userIdentity);

            User user = userIdentity.getUser();
            System.out.println(user.getId());
            user.getRoles().add(Role.Roles.MERCHANT);

            userRepository.save(user);

            return "Request has been approved.";
        } else {
            userIdentityRepository.delete(userIdentity);

            return "Request has been rejected.";
        }
    }
}
