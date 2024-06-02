package com.ch.binarfud.controller;

import org.springframework.web.bind.annotation.RestController;

import com.ch.binarfud.dto.order.request.OrderDto;
import com.ch.binarfud.dto.order.response.AllOrderResponseDto;
import com.ch.binarfud.dto.order.response.OrderResponseDto;
import com.ch.binarfud.model.User;
import com.ch.binarfud.service.OrderFacade;
import com.ch.binarfud.service.UserService;
import com.ch.binarfud.util.ApiResponse;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final UserService userService;

    private final OrderFacade orderFacade;

    public OrderController(UserService userService, OrderFacade orderFacade) {
        this.userService = userService;
        this.orderFacade = orderFacade;
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Object> getAllOrder(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());

        Page<AllOrderResponseDto> ordersPage = orderFacade
                .getAllOrderByUser(user, page, size);

        return ApiResponse.success(HttpStatus.OK, "Orders has successfully retrieved",
                ordersPage.getContent());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Object> getOrderById(@PathVariable UUID id, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());

        OrderResponseDto order = orderFacade.getOrderById(id, user);

        return ApiResponse.success(HttpStatus.OK, "Order has successfully retrieved", order);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Object> addOrder(@Valid @RequestBody OrderDto orderDto, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());

        OrderResponseDto order = orderFacade.addOrder(user, orderDto);

        return ApiResponse.success(HttpStatus.CREATED, "Order has successfully added", order);
    }
}
