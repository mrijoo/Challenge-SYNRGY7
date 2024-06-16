package com.ch.binarfud.dto.product.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateProductDto {
    @NotBlank(message = "name must not be null")
    private String name;

    @NotNull(message = "price must not be null")
    private double price;
}
