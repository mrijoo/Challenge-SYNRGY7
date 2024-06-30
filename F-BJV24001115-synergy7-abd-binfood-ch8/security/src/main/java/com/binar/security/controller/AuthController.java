package com.binar.security.controller;

import org.springframework.web.bind.annotation.RestController;

import com.binar.security.dto.auth.forgot.ForgotPasswordDto;
import com.binar.security.dto.auth.forgot.ForgotPasswordRequestDto;
import com.binar.security.dto.auth.refresh.RefreshTokenDto;
import com.binar.security.dto.auth.signin.SigninDto;
import com.binar.security.dto.auth.signin.SigninResponseDto;
import com.binar.security.dto.auth.signup.ResendVerificationEmailDto;
import com.binar.security.dto.auth.signup.SignupDto;
import com.binar.security.dto.auth.signup.VerificationEmailDto;
import com.binar.security.dto.auth.validate.ValidateTokenDto;
import com.binar.security.service.AuthenticationService;
import com.binar.security.util.ApiResponse;

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

    @PostMapping("/validate-token")
    public ResponseEntity<Object> validateToken(@RequestBody ValidateTokenDto validateTokenDto) {
        return ApiResponse.success(HttpStatus.OK, "Token is valid", authenticationService.validateToken(validateTokenDto));
    }
}
