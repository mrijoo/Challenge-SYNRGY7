package com.ch.binarfud.dto.auth.signin.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SigninDto {
    @NotBlank(message = "username or email must not be null")
    private String username;

    @NotNull(message = "password must not be null")
    private String password;
}
