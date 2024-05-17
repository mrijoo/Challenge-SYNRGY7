package com.ch.binarfud.dto.auth.refresh.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenDto {
    @JsonProperty("refresh_token")
    @NotBlank(message = "refresh_token must not be null")
    private String refreshToken;
}
