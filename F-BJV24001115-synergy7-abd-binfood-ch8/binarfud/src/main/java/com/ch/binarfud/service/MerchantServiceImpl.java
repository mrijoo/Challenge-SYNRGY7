package com.ch.binarfud.service;

import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ch.binarfud.client.SecurityServiceClient;
import com.ch.binarfud.client.data.AccountDetailResponse;
import com.ch.binarfud.client.data.AddIdentityDto;
import com.ch.binarfud.dto.merchant.request.CreateMerchantDto;
import com.ch.binarfud.dto.merchant.request.OpenMerchantDto;
import com.ch.binarfud.dto.merchant.request.RegisterMerchantDto;
import com.ch.binarfud.dto.merchant.request.UpdateMerchantDto;
import com.ch.binarfud.dto.merchant.response.MerchantResponseDto;
import com.ch.binarfud.dto.merchant.response.OpenMerchantResponseDto;
import com.ch.binarfud.exception.DataNotFoundException;
import com.ch.binarfud.model.Merchant;
import com.ch.binarfud.model.User;
import com.ch.binarfud.repository.MerchantRepository;
import com.ch.binarfud.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MerchantServiceImpl implements MerchantService {
    private final ModelMapper modelMapper;

    private final ObjectMapper objectMapper;

    private final MerchantRepository merchantRepository;

    private final UserRepository userRepository;

    private final SecurityServiceClient securityServiceClient;

    public MerchantServiceImpl(ModelMapper modelMapper, ObjectMapper objectMapper,
            MerchantRepository merchantRepository,
            UserRepository userRepository, SecurityServiceClient securityServiceClient) {
        this.modelMapper = modelMapper;
        this.objectMapper = objectMapper;
        this.merchantRepository = merchantRepository;
        this.userRepository = userRepository;
        this.securityServiceClient = securityServiceClient;
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
    public MerchantResponseDto addMerchant(UUID userId, CreateMerchantDto createMerchantDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        if (user.getMerchant() != null) {
            throw new IllegalStateException("User already has a Merchant.");
        }

        Merchant merchant = modelMapper.map(createMerchantDto, Merchant.class);
        merchant.setUser(user);
        Merchant savedMerchant = merchantRepository.save(merchant);

        return modelMapper.map(savedMerchant, MerchantResponseDto.class);
    }

    @Override
    public MerchantResponseDto updateMerchant(UUID userId, UpdateMerchantDto updateMerchantDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        Merchant existingMerchant = Optional.ofNullable(user.getMerchant())
                .orElseThrow(() -> new DataNotFoundException("Merchant not found"));

        modelMapper.map(updateMerchantDto, existingMerchant);
        Merchant savedMerchant = merchantRepository.save(existingMerchant);

        return modelMapper.map(savedMerchant, MerchantResponseDto.class);
    }

    @Override
    public OpenMerchantResponseDto openMerchant(UUID userId, OpenMerchantDto openMerchantDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        Merchant existingMerchant = Optional.ofNullable(user.getMerchant())
                .orElseThrow(() -> new DataNotFoundException("Merchant not found"));

        modelMapper.map(openMerchantDto, existingMerchant);
        Merchant savedMerchant = merchantRepository.save(existingMerchant);

        return modelMapper.map(savedMerchant, OpenMerchantResponseDto.class);
    }

    @Override
    public String registerMerchant(UUID userId, RegisterMerchantDto registerMerchant) {

        ResponseEntity<String> response = securityServiceClient.getAccountDetail(userId);
        AccountDetailResponse accountDetailResponse = null;

        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                accountDetailResponse = objectMapper.readValue(response.getBody(), AccountDetailResponse.class);
            } catch (Exception e) {
                log.error("Failed to parse response from security service", e);
                return "Request to become a merchant has been failed.";
            }

            if (accountDetailResponse.getData().getRoles().contains("MERCHANT")) {
                return "User is already a merchant.";
            } else {
                AddIdentityDto addIdentityDto = new AddIdentityDto();
                addIdentityDto.setUserId(userId);
                addIdentityDto.setFullName(registerMerchant.getFullName());
                addIdentityDto.setIdNumber(registerMerchant.getIdNumber());

                ResponseEntity<String> addIdentityResponse = securityServiceClient.addIdentity(addIdentityDto);

                if (addIdentityResponse.getStatusCode() == HttpStatus.CREATED) {
                    return "Request to become a merchant has been sent.";
                } else if (addIdentityResponse.getStatusCode() == HttpStatus.CONFLICT) {
                    throw new IllegalStateException("Request to become a merchant is still pending.");
                } else {
                    return "Request to become a merchant has been failed.";
                }
            }
        } else {
            return "Request to become a merchant has been failed.";
        }
    }

    // @Override
    // public Page<RequestMerchantResponseDto> getRequestMerchant(int page, int
    // size) {
    // Page<UserIdentity> userIdentities =
    // userIdentityRepository.findByVerified(false, PageRequest.of(page, size));
    // return userIdentities.map(userIdentity -> modelMapper.map(userIdentity,
    // RequestMerchantResponseDto.class));
    // }

    // @Override
    // public RequestMerchantResponseDto getRequestMerchantById(UUID id) {
    // UserIdentity userIdentity = userIdentityRepository.findById(id)
    // .orElseThrow(() -> new DataNotFoundException("Request not found"));
    // return modelMapper.map(userIdentity, RequestMerchantResponseDto.class);
    // }

    // @Override
    // public String approvalRequestMerchant(UUID id, ApprovalMerchantDto
    // approvalMerchantDto) {
    // UserIdentity userIdentity = userIdentityRepository.findById(id)
    // .orElseThrow(() -> new DataNotFoundException("Request not found"));

    // boolean isApproved = approvalMerchantDto.isApproval();

    // if (isApproved) {
    // userIdentity.setVerified(isApproved);
    // userIdentityRepository.save(userIdentity);

    // User user = userIdentity.getUser();
    // // user.getRoles().add(Role.Roles.MERCHANT);

    // userRepository.save(user);

    // return "Request has been approved.";
    // } else {
    // userIdentityRepository.delete(userIdentity);

    // return "Request has been rejected.";
    // }
    // }
}
