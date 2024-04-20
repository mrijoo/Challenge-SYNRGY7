package com.binfood.service;

import com.binfood.model.*;

import lombok.Getter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class OrderService {
    private Order order = new Order();

    public void addOrderItem(MenuItem item, int quantity) {
        order.addItem(item, quantity);
    }

    public List<MenuItem> getMenuItems() {
        return Stream.of(
                new FoodItem("Nasi Goreng", 15000),
                new FoodItem("Mie Goreng", 13000),
                new FoodItem("Nasi + Ayam", 18000),
                new FoodItem("Es Teh Manis", 3000),
                new FoodItem("Es Jeruk", 5000)).collect(Collectors.toList());
    }
}