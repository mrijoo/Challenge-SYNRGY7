package com.binfood.model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Getter;

@Getter
public class Order {
    private Map<String, MenuItem> itemMap = new LinkedHashMap<>();
    private Map<String, Integer> quantityMap = new LinkedHashMap<>();

    public void addItem(MenuItem item, int quantity) {
        String itemName = item.getName();
        if (itemMap.containsKey(itemName)) {
            quantityMap.put(itemName, quantityMap.get(itemName) + quantity);
        } else {
            itemMap.put(itemName, item);
            quantityMap.put(itemName, quantity);
        }
    }

    public Map<MenuItem, Integer> getItems() {
        return itemMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getValue, entry -> quantityMap.get(entry.getKey())));
    }

    public int getTotal() {
        return itemMap.values().stream()
                .mapToInt(item -> item.getPrice() * quantityMap.get(item.getName()))
                .sum();
    }
}