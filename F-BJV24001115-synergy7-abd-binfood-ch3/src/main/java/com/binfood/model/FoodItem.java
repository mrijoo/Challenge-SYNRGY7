package com.binfood.model;

import com.binfood.utils.SystemUtils;

public class FoodItem extends MenuItem {
    public FoodItem(String name, int price) {
        super(name, price);
    }

    @Override
    public String getDetails() {
        return String.format("%s\t\t%s", getName(), SystemUtils.formatPrice(getPrice()));
    }
}