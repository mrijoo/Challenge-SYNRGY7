package com.binfood.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class MenuItem {
    private String name;
    private int price;

    public abstract String getDetails();
}