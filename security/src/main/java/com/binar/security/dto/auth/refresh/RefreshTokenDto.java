package com.binar.security.dto.auth.refresh;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenDto {
    @NotBlank(message = "refresh_token must not be null")
    private String refreshToken;
}
