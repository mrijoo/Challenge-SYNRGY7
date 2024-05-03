package com.ch.binarfud.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import com.ch.binarfud.dto.merchant.ResponseMerchant;
import com.ch.binarfud.dto.merchant.UpdateMerchantDto;
import com.ch.binarfud.model.Merchant;
import com.ch.binarfud.model.User;

public interface MerchantService {
    Page<Merchant> getAllMerchant(int page, int size);

    List<Merchant> getMerchantByStatus(Boolean status);

    ResponseEntity<Merchant> getProfile(User user);

    Merchant getMerchantById(UUID id);

    ResponseEntity<ResponseMerchant> createMerchant(Merchant merchant);

    ResponseEntity<ResponseMerchant> updateMerchant(UpdateMerchantDto updateMerchantDto);

    ResponseEntity<Void> deleteMerchant();
}
