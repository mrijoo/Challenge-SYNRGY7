package com.ch.binarfud.dto.merchant.response;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MerchantResponseDto {
    private UUID id;

    private String name;

    private String location;

    @JsonProperty("open")
    private Boolean isOpen;
}
