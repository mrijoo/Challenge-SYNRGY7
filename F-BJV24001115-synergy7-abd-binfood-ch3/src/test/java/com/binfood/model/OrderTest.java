package com.binfood.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OrderTest {
    private Order order;

    @BeforeEach
    public void setUp() {
        order = new Order();
    }

    @Test
    public void testAddItem() {
        MenuItem item = new FoodItem("Mie Goreng", 13000);
        order.addItem(item, 2);
        assertEquals(1, order.getItems().size());
        assertEquals(26000, order.getTotal());
    }

    @Test
    public void testAddItemTwice() {
        MenuItem item = new FoodItem("Mie Goreng", 13000);
        order.addItem(item, 2);
        order.addItem(item, 3);
        assertEquals(1, order.getItems().size());
        assertEquals(65000, order.getTotal());
    }
}
