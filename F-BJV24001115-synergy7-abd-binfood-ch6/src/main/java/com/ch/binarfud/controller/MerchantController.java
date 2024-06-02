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
import com.ch.binarfud.model.User;
import com.ch.binarfud.service.MerchantService;
import com.ch.binarfud.service.UserService;
import com.ch.binarfud.util.ApiResponse;

import jakarta.validation.Valid;

import java.security.Principal;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/merchants")
public class MerchantController {
    private final UserService userService;
    private final MerchantService merchantService;

    public MerchantController(UserService userService, MerchantService merchantService) {
        this.userService = userService;
        this.merchantService = merchantService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllMerchant(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<MerchantResponseDto> merchantsPage = merchantService.getAllMerchant(page, size);
        return ApiResponse.success(HttpStatus.OK, "Merchants has successfully retrieved",
                merchantsPage.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMerchantById(@PathVariable UUID id) {
        MerchantResponseDto merchant = merchantService.getMerchantById(id);
        return ApiResponse.success(HttpStatus.OK, "Merchant has successfully retrieved", merchant);
    }

    @PostMapping
    @PreAuthorize("hasRole('MERCHANT')")
    public ResponseEntity<Object> addMerchant(@Valid @RequestBody CreateMerchantDto createMerchantDto,
            Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        MerchantResponseDto merchant = merchantService.addMerchant(user, createMerchantDto);

        return ApiResponse.success(HttpStatus.CREATED, "Merchant has successfully added", merchant);
    }

    @PutMapping
    @PreAuthorize("hasRole('MERCHANT')")
    public ResponseEntity<Object> updateMerchant(@Valid @RequestBody UpdateMerchantDto updateMerchantDto,
            Principal principal) {
        User user = userService.getUserByUsername(principal.getName());

        MerchantResponseDto merchant = merchantService.updateMerchant(user, updateMerchantDto);
        return ApiResponse.success(HttpStatus.OK, "Merchant has successfully updated", merchant);
    }

    @PatchMapping("/open")
    @PreAuthorize("hasRole('MERCHANT')")
    public ResponseEntity<Object> openMerchant(@Valid @RequestBody OpenMerchantDto openMerchantDto,
            Principal principal) {
        User user = userService.getUserByUsername(principal.getName());

        OpenMerchantResponseDto merchant = merchantService.openMerchant(user, openMerchantDto);
        return ApiResponse.success(HttpStatus.OK, "Merchant has successfully updated", merchant);
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerMerchant(@Valid @RequestBody RegisterMerchantDto registerMerchant,
            Principal principal) {
        User user = userService.getUserByUsername(principal.getName());

        return ApiResponse.success(HttpStatus.OK, merchantService.registerMerchant(user, registerMerchant));
    }

    @GetMapping("/requests")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> getMerchantRequests(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<RequestMerchantResponseDto> merchantsPage = merchantService.getRequestMerchant(page, size);
        return ApiResponse.success(HttpStatus.OK, "Merchant requests has successfully retrieved",
                merchantsPage.getContent());
    }

    @GetMapping("/requests/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> getMerchantRequestById(@PathVariable UUID id) {
        RequestMerchantResponseDto merchant = merchantService.getRequestMerchantById(id);
        return ApiResponse.success(HttpStatus.OK, "Merchant request has successfully retrieved", merchant);
    }

    @PutMapping("/requests/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> approveMerchantRequest(@PathVariable UUID id, @RequestBody ApprovalMerchantDto approvalMerchant) {
        return ApiResponse.success(HttpStatus.OK, merchantService.approvalRequestMerchant(id, approvalMerchant));
    }
}
