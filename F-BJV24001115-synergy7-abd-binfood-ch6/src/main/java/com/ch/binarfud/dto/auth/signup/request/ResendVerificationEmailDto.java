package com.ch.binarfud.dto.auth.signup.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResendVerificationEmailDto {
    @Email(message = "email not valid")
    @NotBlank(message = "email must not be null")
    private String email;
}
