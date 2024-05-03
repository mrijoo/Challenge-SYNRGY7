package com.ch.binarfud.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ch.binarfud.dto.merchant.ResponseMerchant;
import com.ch.binarfud.dto.merchant.UpdateMerchantDto;
import com.ch.binarfud.model.Merchant;
import com.ch.binarfud.model.User;
import com.ch.binarfud.repository.MerchantRepository;
import com.ch.binarfud.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MerchantServiceImpl implements MerchantService {
    private final UserRepository userRepository;
    private final MerchantRepository merchantRepository;

    public MerchantServiceImpl(UserRepository userRepository, MerchantRepository merchantRepository) {
        this.userRepository = userRepository;
        this.merchantRepository = merchantRepository;
    }

    @Override
    public Page<Merchant> getAllMerchant(int page, int size) {
        return merchantRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public List<Merchant> getMerchantByStatus(Boolean status) {
        log.info("Get merchant by status: {}", status);
        return merchantRepository.findByIsOpen(status);
    }
    @Override
    public ResponseEntity<Merchant> getProfile(User user) {
        if (user != null && user.getMerchant() != null) {
            Merchant userMerchant = user.getMerchant();
            return ResponseEntity.ok(userMerchant);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public Merchant getMerchantById(UUID id) {
        return merchantRepository.findById(id).orElse(null);
    }

    @Override
    public ResponseEntity<ResponseMerchant> createMerchant(Merchant merchant) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = (User) authentication.getPrincipal();

        merchant.setUserId(currentUser.getId());

        currentUser = userRepository.findById(currentUser.getId()).orElse(null);

        if (currentUser != null && currentUser.getMerchant() != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        merchant.setUser(currentUser);

        merchant = merchantRepository.save(merchant);

        ResponseMerchant responseMerchant = new ResponseMerchant();
        responseMerchant.setName(merchant.getName());
        responseMerchant.setLocation(merchant.getLocation());
        responseMerchant.setOpen(merchant.getIsOpen());

        return ResponseEntity.ok(responseMerchant);
    }

    @Override
    public ResponseEntity<ResponseMerchant> updateMerchant(UpdateMerchantDto updateMerchantDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = (User) authentication.getPrincipal();

        currentUser = userRepository.findUserWithMerchantById(currentUser.getId()).orElse(null);

        if (currentUser != null && currentUser.getMerchant() != null) {
            Merchant userMerchant = currentUser.getMerchant();
            userMerchant.setName(updateMerchantDto.getName());
            userMerchant.setLocation(updateMerchantDto.getLocation());
            userMerchant.setIsOpen(updateMerchantDto.isOpen());

            merchantRepository.save(userMerchant);

            ResponseMerchant responseMerchant = new ResponseMerchant();
            responseMerchant.setName(userMerchant.getName());
            responseMerchant.setLocation(userMerchant.getLocation());
            responseMerchant.setOpen(userMerchant.getIsOpen());

            return ResponseEntity.ok(responseMerchant);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Void> deleteMerchant() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = (User) authentication.getPrincipal();

        currentUser = userRepository.findUserWithMerchantById(currentUser.getId()).orElse(null);

        if (currentUser != null && currentUser.getMerchant() != null) {
            Merchant userMerchant = currentUser.getMerchant();
            merchantRepository.delete(userMerchant);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
