package com.ch.binarfud.service;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.ch.binarfud.dto.merchant.request.CreateMerchantDto;
import com.ch.binarfud.dto.merchant.request.OpenMerchantDto;
import com.ch.binarfud.dto.merchant.request.RegisterMerchantDto;
import com.ch.binarfud.dto.merchant.request.UpdateMerchantDto;
import com.ch.binarfud.dto.merchant.response.MerchantResponseDto;
import com.ch.binarfud.dto.merchant.response.OpenMerchantResponseDto;

public interface MerchantService {
    Page<MerchantResponseDto> getAllMerchant(int page, int size);

    MerchantResponseDto getMerchantById(UUID id);

    MerchantResponseDto addMerchant(UUID userId, CreateMerchantDto createMerchantDto);
    
    MerchantResponseDto updateMerchant(UUID userId, UpdateMerchantDto updateMerchantDto);

    OpenMerchantResponseDto openMerchant(UUID userId, OpenMerchantDto openMerchantDto);

    String registerMerchant(UUID userId, RegisterMerchantDto registerMerchant);

    // Page<RequestMerchantResponseDto> getRequestMerchant(int page, int size);

    // RequestMerchantResponseDto getRequestMerchantById(UUID id);

    // String approvalRequestMerchant(UUID id, ApprovalMerchantDto approvalMerchantDto);
}
