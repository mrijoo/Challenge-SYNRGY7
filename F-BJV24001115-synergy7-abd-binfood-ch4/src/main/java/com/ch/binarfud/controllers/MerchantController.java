package com.ch.binarfud.controllers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ch.binarfud.dto.merchant.ResponseMerchant;
import com.ch.binarfud.dto.merchant.UpdateMerchantDto;
import com.ch.binarfud.model.Merchant;
import com.ch.binarfud.model.User;
import com.ch.binarfud.repository.UserRepository;
import com.ch.binarfud.service.MerchantService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/merchants")
public class MerchantController {

    private final MerchantService merchantService;

    private final UserRepository userRepository;

    public MerchantController(MerchantService merchantService, UserRepository userRepository) {
        this.merchantService = merchantService;

        this.userRepository = userRepository;
    }

    @GetMapping("/profile")
    public ResponseEntity<Merchant> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = (User) authentication.getPrincipal();

        currentUser = userRepository.findUserWithMerchantById(currentUser.getId()).orElse(null);
        return merchantService.getProfile(currentUser);
    }

    @GetMapping()
    public Page<Merchant> getAllMerchants(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return merchantService.getAllMerchant(page, size);
    }

    @GetMapping("/status")
    public List<Merchant> getMerchantByStatus(@RequestParam Boolean open) {
        return merchantService.getMerchantByStatus(open);
    }

    @Transactional
    @PostMapping()
    public ResponseEntity<ResponseMerchant> createMerchant(@RequestBody Merchant merchant) {
        return merchantService.createMerchant(merchant);
    }

    @PutMapping()
    public ResponseEntity<ResponseMerchant> updateMerchant(@RequestBody UpdateMerchantDto updateMerchantDTO) {
        return merchantService.updateMerchant(updateMerchantDTO);
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteMerchant() {
        return merchantService.deleteMerchant();
    }
}