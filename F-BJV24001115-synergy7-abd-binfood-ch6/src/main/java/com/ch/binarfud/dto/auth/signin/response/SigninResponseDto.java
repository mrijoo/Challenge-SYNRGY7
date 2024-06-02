package com.ch.binarfud.dto.auth.signin.response;

import java.util.List;

import lombok.Data;

@Data
public class SigninResponseDto {
    private String username;

    private String email;

    private String accessToken;

    private String refreshToken;

    private List<String> roles;
}
