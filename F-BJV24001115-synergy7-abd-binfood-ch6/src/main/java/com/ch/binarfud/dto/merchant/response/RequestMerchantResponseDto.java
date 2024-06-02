package com.ch.binarfud.dto.merchant.response;

import java.util.UUID;

import lombok.Data;

@Data
public class RequestMerchantResponseDto {
    private UUID id;
    private String fullName;
    private String idNumber;
}
