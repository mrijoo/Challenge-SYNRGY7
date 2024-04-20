package com.binfood.service;

import com.binfood.model.FoodItem;
import com.binfood.model.MenuItem;
import com.binfood.model.Order;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

public class OrderServiceTest {
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        orderService = new OrderService();
    }

    @Test
    public void testAddOrderItem() {
        MenuItem menuItem = new FoodItem("Test Item", 10000);
        orderService.addOrderItem(menuItem, 2);

        Order order = orderService.getOrder();
        assertEquals(1, order.getItems().size());
        assertEquals(20000, order.getTotal());
    }

}
