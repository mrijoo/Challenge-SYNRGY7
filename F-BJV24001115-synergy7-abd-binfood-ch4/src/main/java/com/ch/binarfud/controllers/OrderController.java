package com.ch.binarfud.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ch.binarfud.dto.order.CreateOrderDto;
import com.ch.binarfud.model.Order;
import com.ch.binarfud.model.User;
import com.ch.binarfud.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> getAllOrder() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUserId = (User) authentication.getPrincipal();

        return orderService.getAllOrder(currentUserId);
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderDto createOrderDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUserId = (User) authentication.getPrincipal();

        return orderService.createOrder(createOrderDto, currentUserId);
    }
}
