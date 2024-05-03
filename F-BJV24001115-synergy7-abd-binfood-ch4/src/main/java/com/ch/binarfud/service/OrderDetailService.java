package com.ch.binarfud.service;

import java.util.List;
import java.util.UUID;

import com.ch.binarfud.model.OrderDetail;
import com.ch.binarfud.model.User;

public interface OrderDetailService {
    List<OrderDetail> getOrderDetailById(UUID id);

    List<OrderDetail> createOrderDetail(OrderDetail orderDetail, User user);
}
