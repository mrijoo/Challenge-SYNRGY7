package com.ch.binarfud.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ch.binarfud.dto.order.CreateOrderDto;
import com.ch.binarfud.model.Order;
import com.ch.binarfud.model.User;

public interface OrderService {
    List<Order> getAllOrder(User user);

    ResponseEntity<Order> createOrder(CreateOrderDto createOrderDto, User user);
}
