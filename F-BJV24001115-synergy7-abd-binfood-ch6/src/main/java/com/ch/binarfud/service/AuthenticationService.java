package com.ch.binarfud.service;

import com.ch.binarfud.dto.auth.forgot.request.ForgotPasswordDto;
import com.ch.binarfud.dto.auth.forgot.request.ForgotPasswordRequestDto;
import com.ch.binarfud.dto.auth.refresh.request.RefreshTokenDto;
import com.ch.binarfud.dto.auth.refresh.response.RefreshTokenResponseDto;
import com.ch.binarfud.dto.auth.signin.request.SigninDto;
import com.ch.binarfud.dto.auth.signin.response.SigninResponseDto;
import com.ch.binarfud.dto.auth.signup.request.ResendVerificationEmailDto;
import com.ch.binarfud.dto.auth.signup.request.SignupDto;
import com.ch.binarfud.dto.auth.signup.request.VerificationEmailDto;
import com.ch.binarfud.dto.auth.signup.response.SignupResponseDto;

public interface AuthenticationService {
    public SigninResponseDto signin(SigninDto signinDto);

    public SignupResponseDto signup(SignupDto signupDto);
    
    public void resendVerificationEmail(ResendVerificationEmailDto resendEmailDto);

    public void verifyEmail(VerificationEmailDto verificationEmailDto);

    public void forgotPasswordRequest(ForgotPasswordRequestDto forgotPasswordDto);

    public void forgotPassword(ForgotPasswordDto forgotPasswordDto);

    public void signout(String token);

    public RefreshTokenResponseDto refreshToken(RefreshTokenDto refreshTokenDto);
}
