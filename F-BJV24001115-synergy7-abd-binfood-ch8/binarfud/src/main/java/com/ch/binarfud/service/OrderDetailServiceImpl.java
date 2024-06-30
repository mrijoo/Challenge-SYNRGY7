package com.ch.binarfud.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ch.binarfud.model.OrderDetail;
import com.ch.binarfud.repository.OrderDetailRepository;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;

    public OrderDetailServiceImpl(OrderDetailRepository orderDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
    }

    @Override
    public List<OrderDetail> getOrderDetailsByOrderId(UUID id) {
        return orderDetailRepository.findByOrderId(id);
    }

    @Override
    public OrderDetail getByOrderId(UUID id) {
        return orderDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order Detail with id " + id + " not found"));
    }

    @Override
    public OrderDetail addOrderDetail(OrderDetail orderDetail) {
        return orderDetailRepository.save(orderDetail);
    }
}
