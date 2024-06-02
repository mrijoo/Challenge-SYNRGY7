package com.ch.binarfud.dto.merchant.response;

import java.util.UUID;

import lombok.Data;

@Data
public class MerchantResponseDto {
    private UUID id;

    private String name;

    private String location;

    private Boolean open;
}
