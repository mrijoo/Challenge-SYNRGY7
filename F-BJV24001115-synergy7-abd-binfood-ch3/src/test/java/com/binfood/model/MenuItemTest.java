package com.binfood.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MenuItemTest {
    private MenuItem menuItem;

    @BeforeEach
    public void setUp() {
        menuItem = new FoodItem("Mie Goreng", 13000);
    }

    @Test
    public void testGetName() {
        assertEquals("Mie Goreng", menuItem.getName());
    }

    @Test
    public void testGetPrice() {
        assertEquals(13000, menuItem.getPrice());
    }
}
