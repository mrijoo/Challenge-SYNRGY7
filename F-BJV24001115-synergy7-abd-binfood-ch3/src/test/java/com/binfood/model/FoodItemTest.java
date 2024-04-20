package com.binfood.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FoodItemTest {
    private FoodItem foodItem;

    @BeforeEach
    public void setUp() {
        foodItem = new FoodItem("Mie Goreng", 13000);
    }

    @Test
    public void testgetDetails () {
        assertEquals("Mie Goreng\t\t13.000", foodItem.getDetails());
    }
}
