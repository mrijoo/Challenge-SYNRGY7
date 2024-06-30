package com.ch.binarfud.client.data;

import java.util.List;
import java.util.UUID;

import lombok.Getter;

@Getter
public class AccountDetailResponse {
    private Data data;
    private String message;
    private String status;

    @Getter
    public class Data {
        private UUID id;
        private String username;
        private String email;
        private List<String> roles;

        private UserIdentity userIdentity;
    }

    @Getter
    public class UserIdentity {
        private String fullName;
        private String idNumber;
        private boolean isVerified;
    }
}
