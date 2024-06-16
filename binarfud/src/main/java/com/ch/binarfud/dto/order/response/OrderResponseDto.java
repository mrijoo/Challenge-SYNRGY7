package com.ch.binarfud.dto.order.response;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.ch.binarfud.model.Order.PaymentMethod;
import com.ch.binarfud.model.Order.Status;

import lombok.Data;

@Data
public class OrderResponseDto {
    private UUID id;

    private UUID userId;

    private String address;

    private PaymentMethod paymentMethod;

    private Status status;

    private double totalPrice;

    private Date createdAt;

    private List<OrderDetails> details;

    @Data
    public static class OrderDetails {
        private UUID productId;

        private String productName;

        private double price;

        private int quantity;
    }
}
