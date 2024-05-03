package com.ch.binarfud.dto.product;
import lombok.Data;

@Data
public class ResponseProduct {
    private String id;
    private String name;
    private double price;

    private String merchantId;

    private String merchantName;

    public ResponseProduct(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.merchantId = merchantId;
        this.merchantName = merchantName;
    }

    public ResponseProduct() {
    }
}
