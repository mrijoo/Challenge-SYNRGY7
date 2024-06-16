package com.ch.binarfud.controller;

import org.springframework.web.bind.annotation.RestController;

import com.ch.binarfud.dto.order.request.OrderDto;
import com.ch.binarfud.dto.order.response.AllOrderResponseDto;
import com.ch.binarfud.dto.order.response.OrderResponseDto;
import com.ch.binarfud.service.OrderFacade;
import com.ch.binarfud.util.ApiResponse;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderFacade orderFacade;

    public OrderController(OrderFacade orderFacade) {
        this.orderFacade = orderFacade;
    }

    @GetMapping("/_user")
    public ResponseEntity<Object> getAllOrder(@RequestHeader("user_id") String userId, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<AllOrderResponseDto> ordersPage = orderFacade
                .getAllOrderByUser(UUID.fromString(userId), page, size);

        return ApiResponse.success(HttpStatus.OK, "Orders has successfully retrieved",
                ordersPage.getContent());
    }

    @GetMapping("/{id}/_user")
    public ResponseEntity<Object> getOrderById(@RequestHeader("user_id") String userId, @PathVariable UUID id) {
        OrderResponseDto order = orderFacade.getOrderById(id, UUID.fromString(userId));

        return ApiResponse.success(HttpStatus.OK, "Order has successfully retrieved", order);
    }

    @PostMapping("/_user")
    public ResponseEntity<Object> addOrder(@RequestHeader("user_id") String userId, @Valid @RequestBody OrderDto orderDto) {
        OrderResponseDto order = orderFacade.addOrder(UUID.fromString(userId), orderDto);

        return ApiResponse.success(HttpStatus.CREATED, "Order has successfully added", order);
    }
}
