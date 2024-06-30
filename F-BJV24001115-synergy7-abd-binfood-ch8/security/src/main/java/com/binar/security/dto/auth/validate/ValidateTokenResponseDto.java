package com.binar.security.dto.auth.validate;

import java.util.List;
import java.util.UUID;

import com.binar.security.enums.UserRole;

import lombok.Data;

@Data
public class ValidateTokenResponseDto {
    private UUID userId;
    private List<UserRole> roles;
}

