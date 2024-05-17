package com.ch.binarfud.dto.merchant.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateMerchantDto {
    @NotBlank(message = "name must not be null")
    private String name;

    @NotBlank(message = "location must not be null")
    private String location;

    @JsonProperty("open")
    @NotNull(message = "open must not be null")
    private Boolean isOpen;
}
