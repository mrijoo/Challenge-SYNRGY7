package com.ch.binarfud.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ch.binarfud.dto.order.CreateOrderDto;
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
    public List<Order> getAllOrder(User user) {
        return orderRepository.findByUserId(user.getId());
    }

    @Override
    public ResponseEntity<Order> createOrder(CreateOrderDto createOrderDto, User user) {
        Order order = new Order();
        order.setUserId(user.getId());
        order.setAddress(createOrderDto.getAddress());
        order.setStatus(createOrderDto.getStatus());
        order.setPaymentMethod(createOrderDto.getPaymentMethod());

        Order savedOrder = orderRepository.save(order);

        return ResponseEntity.ok(savedOrder);
    }
}
