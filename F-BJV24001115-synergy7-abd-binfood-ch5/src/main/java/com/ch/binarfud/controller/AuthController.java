package com.ch.binarfud.controller;

import org.springframework.web.bind.annotation.RestController;

import com.ch.binarfud.dto.auth.refresh.request.RefreshTokenDto;
import com.ch.binarfud.dto.auth.signin.request.SigninDto;
import com.ch.binarfud.dto.auth.signin.response.SigninResponseDto;
import com.ch.binarfud.dto.auth.signup.request.SignupDto;
import com.ch.binarfud.service.AuthenticationService;
import com.ch.binarfud.util.ApiResponse;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(value = "/signin")
    public ResponseEntity<Object> signin(@Valid @RequestBody SigninDto signinDto) {
        SigninResponseDto response = authenticationService.signin(signinDto);
        return ApiResponse.success(HttpStatus.OK, "Signin success", response);
    }

    @PostMapping(value = "/signup")
    public ResponseEntity<Object> signup(@Valid @RequestBody SignupDto signupDto) {
        return ApiResponse.success(HttpStatus.OK, "Signup success", authenticationService.signup(signupDto));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Object> refreshToken(@Valid @RequestBody RefreshTokenDto refreshTokenDto) {
        return ApiResponse.success(HttpStatus.OK, "Refresh token success", authenticationService.refreshToken(refreshTokenDto));
    }
    
}
