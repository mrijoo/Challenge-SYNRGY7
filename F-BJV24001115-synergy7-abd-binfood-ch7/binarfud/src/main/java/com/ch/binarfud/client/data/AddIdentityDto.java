package com.ch.binarfud.client.data;

import java.util.UUID;

import lombok.Data;

@Data
public class AddIdentityDto {
    private UUID userId;
    private String fullName;
    private String idNumber;
}
