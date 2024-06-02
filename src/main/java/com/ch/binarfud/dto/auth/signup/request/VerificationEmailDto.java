package com.ch.binarfud.dto.auth.signup.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerificationEmailDto {
    @NotBlank(message = "otp must not be null")
    private String otp;
}
