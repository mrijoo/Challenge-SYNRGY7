package com.binar.security.dto.account.identity;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddIdentityDto {
    @NotNull
    private UUID userId;

    @NotBlank
    private String fullName;

    @NotBlank
    private String idNumber;
}
