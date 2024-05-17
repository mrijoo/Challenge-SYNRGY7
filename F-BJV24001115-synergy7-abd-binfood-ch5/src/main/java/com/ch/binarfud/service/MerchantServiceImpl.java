package com.ch.binarfud.service;

import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ch.binarfud.dto.merchant.request.CreateMerchantDto;
import com.ch.binarfud.dto.merchant.request.OpenMerchantDto;
import com.ch.binarfud.dto.merchant.request.UpdateMerchantDto;
import com.ch.binarfud.dto.merchant.response.MerchantResponseDto;
import com.ch.binarfud.dto.merchant.response.OpenMerchantResponseDto;
import com.ch.binarfud.exception.DataNotFoundException;
import com.ch.binarfud.model.Merchant;
import com.ch.binarfud.model.User;
import com.ch.binarfud.repository.MerchantRepository;

@Service
public class MerchantServiceImpl implements MerchantService {

    private final ModelMapper modelMapper;

    private final MerchantRepository merchantRepository;

    public MerchantServiceImpl(ModelMapper modelMapper, MerchantRepository merchantRepository) {
        this.modelMapper = modelMapper;
        this.merchantRepository = merchantRepository;
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
}
