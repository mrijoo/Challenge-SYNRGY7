package com.ch.binarfud.dto.merchant;

import lombok.Data;

@Data
public class UpdateMerchantDto {
    private String name;

    private String location;

    private boolean isOpen;
}
