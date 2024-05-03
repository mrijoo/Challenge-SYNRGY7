package com.ch.binarfud.dto.order;

import com.ch.binarfud.model.Order.PaymentMethod;
import com.ch.binarfud.model.Order.Status;

import lombok.Data;

@Data
public class CreateOrderDto {
    private String address;
    private Status status;
    private PaymentMethod paymentMethod;
}
