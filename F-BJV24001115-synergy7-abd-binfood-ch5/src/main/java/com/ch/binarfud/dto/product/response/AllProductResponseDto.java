package com.ch.binarfud.dto.product.response;

import java.util.UUID;

import lombok.Data;

@Data
class Merchant {
    private String name;

    private String location;
}


@Data
public class AllProductResponseDto {
    private UUID id;

    private String name;

    private double price;

    private Merchant merchant;
}
