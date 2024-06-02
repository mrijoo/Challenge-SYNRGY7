package com.ch.binarfud.dto.product.response;

import java.util.UUID;

import lombok.Data;

@Data
public class ProductResponseDto {
    private UUID id;

    private String name;

    private double price;
}
