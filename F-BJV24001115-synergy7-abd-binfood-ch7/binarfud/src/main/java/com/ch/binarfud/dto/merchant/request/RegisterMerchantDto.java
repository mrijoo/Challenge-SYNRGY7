package com.ch.binarfud.dto.merchant.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterMerchantDto {
    @NotBlank(message = "full_name must not be null")
    private String fullName;

    @NotBlank(message = "id_number must not be null")
    private String idNumber;
}
