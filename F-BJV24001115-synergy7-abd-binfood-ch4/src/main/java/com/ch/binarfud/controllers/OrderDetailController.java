package com.ch.binarfud.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.ch.binarfud.model.OrderDetail;
import com.ch.binarfud.service.OrderDetailService;

public class OrderDetailController {
    private final OrderDetailService orderDetailService;

    public OrderDetailController(OrderDetailService orderDetailService) {
        this.orderDetailService = orderDetailService;
    }

    @GetMapping("order/{id}")
    public List<OrderDetail> getOrderDetailById(@PathVariable UUID id) {
        return orderDetailService.getOrderDetailById(id);
    }

    @PostMapping("order/{id}")
    public List<OrderDetail> createOrderDetail(@PathVariable UUID id) {
        return orderDetailService.getOrderDetailById(id);
    }
}
