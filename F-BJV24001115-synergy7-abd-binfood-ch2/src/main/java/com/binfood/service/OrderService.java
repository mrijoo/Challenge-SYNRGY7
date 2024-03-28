package com.binfood.service;

import com.binfood.model.*;

import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

@Getter
public class OrderService {
    private Order order = new Order();

    public void addOrderItem(MenuItem item, int quantity) {
        order.addItem(item, quantity);
    }

    public List<MenuItem> getMenuItems() {
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new FoodItem("Nasi Goreng", 15000));
        menuItems.add(new FoodItem("Mie Goreng", 13000));
        menuItems.add(new FoodItem("Nasi + Ayam", 18000));
        menuItems.add(new FoodItem("Es Teh Manis", 3000));
        menuItems.add(new FoodItem("Es Jeruk", 5000));

        return menuItems;
    }
}