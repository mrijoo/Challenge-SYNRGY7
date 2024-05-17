package com.ch.binarfud.dto.merchant.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OpenMerchantResponseDto {
    @JsonProperty("open")
    private Boolean isOpen;
}
