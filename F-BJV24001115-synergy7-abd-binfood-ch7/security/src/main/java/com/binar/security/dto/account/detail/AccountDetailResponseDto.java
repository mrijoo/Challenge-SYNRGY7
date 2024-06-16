package com.binar.security.dto.account.detail;

import java.util.List;

import com.binar.security.enums.UserRole;

import lombok.Data;

@Data
public class AccountDetailResponseDto {
    private String id;
    private String username;
    private String email;
    private List<UserRole> roles;

    private UserIdentity userIdentity;

    @Data
    class UserIdentity {
        private String fullName;
        private String idNumber;
        private boolean verified;
    }
}
