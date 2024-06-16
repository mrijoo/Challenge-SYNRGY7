package com.binar.security.dto.auth.validate;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ValidateTokenDto {
    @NotBlank(message = "token must not be null")
    private String token;
}
