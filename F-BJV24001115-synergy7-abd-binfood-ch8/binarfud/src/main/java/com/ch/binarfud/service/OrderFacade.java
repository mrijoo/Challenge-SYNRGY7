package com.ch.binarfud.service;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.ch.binarfud.dto.order.request.OrderDto;
import com.ch.binarfud.dto.order.response.AllOrderResponseDto;
import com.ch.binarfud.dto.order.response.OrderResponseDto;

public interface OrderFacade {
    Page<AllOrderResponseDto> getAllOrderByUser(UUID userId, int page, int size);

    OrderResponseDto getOrderById(UUID id, UUID userId);

    OrderResponseDto addOrder(UUID userId, OrderDto orderDto);
}
