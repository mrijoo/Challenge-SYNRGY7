package com.ch.binarfud.service;

import com.ch.binarfud.dto.auth.refresh.request.RefreshTokenDto;
import com.ch.binarfud.dto.auth.refresh.response.RefreshTokenResponseDto;
import com.ch.binarfud.dto.auth.signin.request.SigninDto;
import com.ch.binarfud.dto.auth.signin.response.SigninResponseDto;
import com.ch.binarfud.dto.auth.signup.request.SignupDto;
import com.ch.binarfud.dto.auth.signup.response.SignupResponseDto;

public interface AuthenticationService {
    public SigninResponseDto signin(SigninDto signinDto);
    public SignupResponseDto signup(SignupDto signupDto);
    public RefreshTokenResponseDto refreshToken(RefreshTokenDto refreshTokenDto);
}
