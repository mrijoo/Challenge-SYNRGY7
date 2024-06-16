package com.binar.security.controller;

import org.springframework.web.bind.annotation.RestController;

import com.binar.security.dto.account.identity.AddIdentityDto;
import com.binar.security.service.AccountService;
import com.binar.security.util.ApiResponse;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Object> getAccountDetail(@PathVariable("id") UUID id) {
        return ApiResponse.success(HttpStatus.OK, "Account detail", accountService.getAccountDetail(id));
    }

    @PostMapping("/identity")
    public ResponseEntity<Object> addUserIdentity(@Valid @RequestBody AddIdentityDto addIdentityDto) {
        return ApiResponse.success(HttpStatus.CREATED, "User identity added", accountService.addUserIdentity(addIdentityDto));
    }
}
