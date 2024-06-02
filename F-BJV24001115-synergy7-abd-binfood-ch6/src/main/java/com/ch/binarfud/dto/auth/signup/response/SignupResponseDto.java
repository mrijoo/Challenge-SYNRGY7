package com.ch.binarfud.dto.auth.signup.response;

import java.util.List;

import lombok.Data;

@Data
public class SignupResponseDto {
    private String username;

    private String email;

    private String accessToken;

    private String refreshToken;

        private List<String> roles;
}
