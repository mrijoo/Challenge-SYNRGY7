package com.ch.binarfud.dto.merchant.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OpenMerchantDto {
    @NotNull(message = "open must not be null")
    private Boolean open;
}
