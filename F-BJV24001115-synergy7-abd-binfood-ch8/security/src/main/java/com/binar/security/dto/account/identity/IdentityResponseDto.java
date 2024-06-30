package com.binar.security.dto.account.identity;

import lombok.Data;

@Data
public class IdentityResponseDto {
    private String fullName;
    private String idNumber;
    private boolean verified;
}
