package com.binfood.controller;

import com.binfood.model.Order;
import com.binfood.service.OrderService;
import com.binfood.view.OrderView;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OrderControllerTest {
    private OrderController orderController;
    private OrderView view;

    @BeforeEach
    public void setUp() {
        OrderService orderService = new OrderService();

        view = new OrderView();
        orderController = new OrderController(orderService, view);
    }

    @Test
    public void displayMenu() {
        orderController.displayMenu();
    }
    @Test
    public void testEditOrder() {
        orderController.addOrderItem(1, 1);

        orderController.editOrder(true, "1", "4");

        Order order = orderController.getOrderService().getOrder();
        assertNotNull(order);
        assertEquals(1, order.getItems().size());
        assertEquals(4, order.getQuantityMap().get("Nasi Goreng"));
    }

    @Test
    public void testEditOrderRemove() {
        orderController.addOrderItem(1, 1);
        orderController.addOrderItem(2, 2);

        orderController.editOrder(true, "2", null);

        Order order = orderController.getOrderService().getOrder();
        assertNotNull(order);
        assertEquals(1, order.getItems().size());
    }

    @Test
    public void testConfirmAndPay() {
        orderController.addOrderItem(1, 2);

        orderController.confirmAndPay();

        Order order = orderController.getOrderService().getOrder();
        assertNotNull(order);
        assertEquals(1, order.getItems().size());
    }
}
