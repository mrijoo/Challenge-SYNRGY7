package com.ch.binarfud.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ch.binarfud.exception.DataNotFoundException;
import com.ch.binarfud.model.Order;
import com.ch.binarfud.model.User;
import com.ch.binarfud.repository.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Page<Order> getAllOrderByUser(User user, int page, int size) {
        return orderRepository.findByUserId(user.getId(), PageRequest.of(page, size));
    }

    @Override
    public Order getOrderById(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Order with id " + id + " not found"));
    }

    public Order addOrder(Order order) {
        return orderRepository.save(order);
    }

}
