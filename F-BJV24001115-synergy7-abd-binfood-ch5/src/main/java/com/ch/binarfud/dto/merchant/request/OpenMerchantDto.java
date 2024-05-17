package com.ch.binarfud.dto.merchant.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OpenMerchantDto {
    @JsonProperty("open")
    @NotNull(message = "open must not be null")
    private Boolean isOpen;
}
