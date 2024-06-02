package com.ch.binarfud.controller;

import org.springframework.web.bind.annotation.RestController;

import com.ch.binarfud.dto.auth.forgot.request.ForgotPasswordDto;
import com.ch.binarfud.dto.auth.forgot.request.ForgotPasswordRequestDto;
import com.ch.binarfud.dto.auth.refresh.request.RefreshTokenDto;
import com.ch.binarfud.dto.auth.signin.request.SigninDto;
import com.ch.binarfud.dto.auth.signin.response.SigninResponseDto;
import com.ch.binarfud.dto.auth.signup.request.ResendVerificationEmailDto;
import com.ch.binarfud.dto.auth.signup.request.SignupDto;
import com.ch.binarfud.dto.auth.signup.request.VerificationEmailDto;
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

    @PostMapping("/verify/resend")
    public ResponseEntity<Object> resendVerificationEmail(@Valid @RequestBody ResendVerificationEmailDto resendEmailDto) {
        authenticationService.resendVerificationEmail(resendEmailDto);
        return ApiResponse.success(HttpStatus.OK, "Verification email sent");
    }

    @PostMapping("/verify")
    public ResponseEntity<Object> verifyEmail(@Valid @RequestBody VerificationEmailDto verificationEmailDto) {
        authenticationService.verifyEmail(verificationEmailDto);
        return ApiResponse.success(HttpStatus.OK, "Email verified");
    }

    @PostMapping("/forgot-password/request")
    public ResponseEntity<Object> forgotPasswordRequest(@Valid @RequestBody ForgotPasswordRequestDto forgotPasswordRequestDto) {
        authenticationService.forgotPasswordRequest(forgotPasswordRequestDto);
        return ApiResponse.success(HttpStatus.OK, "Forgot password success");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Object> forgotPassword(@Valid @RequestBody ForgotPasswordDto forgotPasswordDto) {
        authenticationService.forgotPassword(forgotPasswordDto);
        return ApiResponse.success(HttpStatus.OK, "Forgot password success");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Object> refreshToken(@Valid @RequestBody RefreshTokenDto refreshTokenDto) {
        return ApiResponse.success(HttpStatus.OK, "Refresh token success", authenticationService.refreshToken(refreshTokenDto));
    }
}
