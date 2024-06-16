package com.ch.binarfud.dto.order.request;

import com.ch.binarfud.model.Order.PaymentMethod;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class OrderDto {
    @NotBlank(message = "address must not be null")
    private String address;

    @NotNull(message = "payment_method must not be null")
    private PaymentMethod paymentMethod;

    @NotNull(message = "details must not be null")
    private List<DetailOrder> details;

    @Data
    public static class DetailOrder {
        @NotNull(message = "product_id must not be null")
        private UUID productId;

        @NotNull(message = "quantity must not be null")
        private int quantity;
    }
}
