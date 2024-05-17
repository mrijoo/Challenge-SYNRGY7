package com.ch.binarfud.dto.order.response;

import java.util.List;
import java.util.UUID;

import com.ch.binarfud.model.Order.PaymentMethod;
import com.ch.binarfud.model.Order.Status;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AllOrderResponseDto {
    private UUID id;

    private String address;

    @JsonProperty("payment_method")
    private PaymentMethod paymentMethod;

    private Status status;

    @JsonProperty("total_price")
    private double totalPrice;

    @JsonProperty("order_date")
    private String updatedAt;

    private List<OrderDetails> details;

    @Data
    public static class OrderDetails {
        @JsonProperty("product_id")
        private UUID productId;

        @JsonProperty("product_name")
        private String productName;

        private double price;

        private int quantity;
    }
}
