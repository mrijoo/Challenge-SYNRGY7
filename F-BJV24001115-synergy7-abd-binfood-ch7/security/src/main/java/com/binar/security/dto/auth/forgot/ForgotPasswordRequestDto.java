package com.binar.security.dto.auth.forgot;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotPasswordRequestDto {
    @NotBlank(message = "username or email must not be null")
    private String username;
}
