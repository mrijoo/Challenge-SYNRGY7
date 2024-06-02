package com.ch.binarfud.service;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.ch.binarfud.model.Order;
import com.ch.binarfud.model.User;

public interface OrderService {
    Page<Order> getAllOrderByUser(User user, int page, int size);

    Order getOrderById(UUID id);

    Order addOrder(Order order);
}