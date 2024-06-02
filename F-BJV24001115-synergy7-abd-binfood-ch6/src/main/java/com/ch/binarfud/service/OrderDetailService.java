package com.ch.binarfud.service;

import java.util.List;
import java.util.UUID;

import com.ch.binarfud.model.OrderDetail;

public interface OrderDetailService {
    public List<OrderDetail> getOrderDetailsByOrderId(UUID id);

    public OrderDetail getByOrderId(UUID id);

    public OrderDetail addOrderDetail(OrderDetail orderDetail);

}
