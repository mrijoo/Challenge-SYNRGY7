package com.ch.binarfud.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ch.binarfud.model.OrderDetail;
import com.ch.binarfud.model.User;
import com.ch.binarfud.repository.OrderDetailRepository;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    private OrderDetailRepository orderDetailRepository;

    public OrderDetailServiceImpl(OrderDetailRepository orderDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
    }

    @Override
    public List<OrderDetail> getOrderDetailById(UUID id) {
        return orderDetailRepository.findByOrderId(id);
    }

    @Override
    public List<OrderDetail> createOrderDetail(OrderDetail orderDetail, User user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createOrderDetail'");
    }

}