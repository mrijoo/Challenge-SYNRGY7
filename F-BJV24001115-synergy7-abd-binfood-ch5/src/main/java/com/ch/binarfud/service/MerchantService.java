package com.ch.binarfud.service;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.ch.binarfud.dto.merchant.request.CreateMerchantDto;
import com.ch.binarfud.dto.merchant.request.OpenMerchantDto;
import com.ch.binarfud.dto.merchant.request.UpdateMerchantDto;
import com.ch.binarfud.dto.merchant.response.MerchantResponseDto;
import com.ch.binarfud.dto.merchant.response.OpenMerchantResponseDto;
import com.ch.binarfud.model.User;

public interface MerchantService {
    Page<MerchantResponseDto> getAllMerchant(int page, int size);

    MerchantResponseDto getMerchantById(UUID id);

    MerchantResponseDto addMerchant(User user, CreateMerchantDto createMerchantDto);
    
    MerchantResponseDto updateMerchant(User user, UpdateMerchantDto updateMerchantDto);

    OpenMerchantResponseDto openMerchant(User user, OpenMerchantDto openMerchantDto);
}
