package com.ch.binarfud.dto.order.response;

import java.util.List;
import java.util.UUID;

import com.ch.binarfud.model.Order.PaymentMethod;
import com.ch.binarfud.model.Order.Status;

import lombok.Data;

@Data
public class AllOrderResponseDto {
    private UUID id;

    private String address;

    private PaymentMethod paymentMethod;

    private Status status;

    private double totalPrice;

    private String updatedAt;

    private List<OrderDetails> details;

    @Data
    public static class OrderDetails {
        private UUID productId;

        private String productName;

        private double price;

        private int quantity;
    }
}
