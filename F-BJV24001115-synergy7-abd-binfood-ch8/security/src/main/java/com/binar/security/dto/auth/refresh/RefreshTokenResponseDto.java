package com.binar.security.dto.auth.refresh;

import lombok.Data;

@Data
public class RefreshTokenResponseDto {
    private String accessToken;

    private String refreshToken;
}
