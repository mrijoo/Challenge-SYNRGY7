package com.ch.binarfud.dto.merchant.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApprovalMerchantDto {
    @NotNull(message = "approval must not be null")
    private boolean approval;
}
