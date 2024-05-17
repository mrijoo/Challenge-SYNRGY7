package com.ch.binarfud.service;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.ch.binarfud.dto.order.request.OrderDto;
import com.ch.binarfud.dto.order.response.AllOrderResponseDto;
import com.ch.binarfud.dto.order.response.OrderResponseDto;
import com.ch.binarfud.model.User;

public interface OrderFacade {
    Page<AllOrderResponseDto> getAllOrderByUser(User user, int page, int size);

    OrderResponseDto getOrderById(UUID id, User user);

    OrderResponseDto addOrder(User user, OrderDto orderDto);
}
