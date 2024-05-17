package com.ch.binarfud.dto.order.response;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.ch.binarfud.model.Order.PaymentMethod;
import com.ch.binarfud.model.Order.Status;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OrderResponseDto {
    private UUID id;

    @JsonProperty("user_id")
    private UUID userId;

    private String address;

    @JsonProperty("payment_method")
    private PaymentMethod paymentMethod;

    private Status status;

    @JsonProperty("total_price")
    private double totalPrice;

    @JsonProperty("date_time")
    private Date createdAt;

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
