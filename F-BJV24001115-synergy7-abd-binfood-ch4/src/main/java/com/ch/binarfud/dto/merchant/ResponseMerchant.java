package com.ch.binarfud.dto.merchant;

import java.util.UUID;

import lombok.Data;

@Data
public class ResponseMerchant {
    private UUID id;

    private String name;

    private String location;

    private boolean isOpen;
}
