package com.binar.security.service;

import com.binar.security.dto.auth.forgot.ForgotPasswordDto;
import com.binar.security.dto.auth.forgot.ForgotPasswordRequestDto;
import com.binar.security.dto.auth.refresh.RefreshTokenDto;
import com.binar.security.dto.auth.refresh.RefreshTokenResponseDto;
import com.binar.security.dto.auth.signin.SigninDto;
import com.binar.security.dto.auth.signin.SigninResponseDto;
import com.binar.security.dto.auth.signup.ResendVerificationEmailDto;
import com.binar.security.dto.auth.signup.SignupDto;
import com.binar.security.dto.auth.signup.SignupResponseDto;
import com.binar.security.dto.auth.signup.VerificationEmailDto;
import com.binar.security.dto.auth.validate.ValidateTokenDto;
import com.binar.security.dto.auth.validate.ValidateTokenResponseDto;

public interface AuthenticationService {
    public SigninResponseDto signin(SigninDto signinDto);

    public SignupResponseDto signup(SignupDto signupDto);
    
    public void resendVerificationEmail(ResendVerificationEmailDto resendEmailDto);

    public void verifyEmail(VerificationEmailDto verificationEmailDto);

    public void forgotPasswordRequest(ForgotPasswordRequestDto forgotPasswordDto);

    public void forgotPassword(ForgotPasswordDto forgotPasswordDto);

    public void signout(String token);

    public RefreshTokenResponseDto refreshToken(RefreshTokenDto refreshTokenDto);

    public ValidateTokenResponseDto validateToken(ValidateTokenDto validateTokenDto);
}
