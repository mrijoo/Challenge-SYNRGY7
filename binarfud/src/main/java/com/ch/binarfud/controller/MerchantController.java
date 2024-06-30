package com.ch.binarfud.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ch.binarfud.dto.merchant.request.ApprovalMerchantDto;
import com.ch.binarfud.dto.merchant.request.CreateMerchantDto;
import com.ch.binarfud.dto.merchant.request.OpenMerchantDto;
import com.ch.binarfud.dto.merchant.request.RegisterMerchantDto;
import com.ch.binarfud.dto.merchant.request.UpdateMerchantDto;
import com.ch.binarfud.dto.merchant.response.MerchantResponseDto;
import com.ch.binarfud.dto.merchant.response.OpenMerchantResponseDto;
import com.ch.binarfud.dto.merchant.response.RequestMerchantResponseDto;
import com.ch.binarfud.service.MerchantService;
import com.ch.binarfud.util.ApiResponse;

import jakarta.validation.Valid;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/api/v1/merchants")
public class MerchantController {
    private final MerchantService merchantService;

    public MerchantController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @GetMapping("/_public")
    public ResponseEntity<Object> getAllMerchant(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<MerchantResponseDto> merchantsPage = merchantService.getAllMerchant(page, size);
        return ApiResponse.success(HttpStatus.OK, "Merchants has successfully retrieved",
                merchantsPage.getContent());
    }

    @GetMapping("/{id}/_public")
    public ResponseEntity<Object> getMerchantById(@PathVariable UUID id) {
        MerchantResponseDto merchant = merchantService.getMerchantById(id);
        return ApiResponse.success(HttpStatus.OK, "Merchant has successfully retrieved", merchant);
    }

    @PostMapping("/_merchant")
    public ResponseEntity<Object> addMerchant(@RequestHeader("user_id") String userId, @Valid @RequestBody CreateMerchantDto createMerchantDto) {
        MerchantResponseDto merchant = merchantService.addMerchant(UUID.fromString(userId), createMerchantDto);

        return ApiResponse.success(HttpStatus.CREATED, "Merchant has successfully added", merchant);
    }

    @PutMapping("/_merchant")
    public ResponseEntity<Object> updateMerchant(@RequestHeader("user_id") String userId, @Valid @RequestBody UpdateMerchantDto updateMerchantDto) {
        MerchantResponseDto merchant = merchantService.updateMerchant(UUID.fromString(userId), updateMerchantDto);
        return ApiResponse.success(HttpStatus.OK, "Merchant has successfully updated", merchant);
    }

    @PatchMapping("/open/_merchant")
    public ResponseEntity<Object> openMerchant(@RequestHeader("user_id") String userId, @Valid @RequestBody OpenMerchantDto openMerchantDto) {
        OpenMerchantResponseDto merchant = merchantService.openMerchant(UUID.fromString(userId), openMerchantDto);
        return ApiResponse.success(HttpStatus.OK, "Merchant has successfully updated", merchant);
    }

    @PostMapping("/register/_user")
    public ResponseEntity<Object> registerMerchant(@RequestHeader("user_id") String userId, @Valid @RequestBody RegisterMerchantDto registerMerchant) {
        return ApiResponse.success(HttpStatus.OK, merchantService.registerMerchant(UUID.fromString(userId), registerMerchant));
    }

    // @GetMapping("/requests/_admin")
    // public ResponseEntity<Object> getMerchantRequests(@RequestParam(defaultValue = "0") int page,
    //         @RequestParam(defaultValue = "10") int size) {
    //     Page<RequestMerchantResponseDto> merchantsPage = merchantService.getRequestMerchant(page, size);
    //     return ApiResponse.success(HttpStatus.OK, "Merchant requests has successfully retrieved",
    //             merchantsPage.getContent());
    // }

    // @GetMapping("/requests/{id}/_admin")
    // public ResponseEntity<Object> getMerchantRequestById(@PathVariable UUID id) {
    //     RequestMerchantResponseDto merchant = merchantService.getRequestMerchantById(id);
    //     return ApiResponse.success(HttpStatus.OK, "Merchant request has successfully retrieved", merchant);
    // }

    // @PutMapping("/requests/{id}/_admin")
    // public ResponseEntity<Object> approveMerchantRequest(@PathVariable UUID id, @RequestBody ApprovalMerchantDto approvalMerchant) {
    //     return ApiResponse.success(HttpStatus.OK, merchantService.approvalRequestMerchant(id, approvalMerchant));
    // }
}
