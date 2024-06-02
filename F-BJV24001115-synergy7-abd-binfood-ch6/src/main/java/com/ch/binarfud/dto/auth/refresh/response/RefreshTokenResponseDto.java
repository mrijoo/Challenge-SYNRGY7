package com.ch.binarfud.dto.auth.refresh.response;

import lombok.Data;

@Data
public class RefreshTokenResponseDto {
    private String accessToken;

    private String refreshToken;
}
