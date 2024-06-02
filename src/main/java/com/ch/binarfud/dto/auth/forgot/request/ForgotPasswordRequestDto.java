package com.ch.binarfud.dto.auth.forgot.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotPasswordRequestDto {
    @NotBlank(message = "username or email must not be null")
    private String username;
}
