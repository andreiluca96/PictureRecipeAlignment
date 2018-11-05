package com.example.lucaandrei.picturerecipealignment.model;

import java.util.Objects;

public class Ingredient {
    private String name;
    private int quantity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        Objects.requireNonNull(name);
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("The quantity value must be positive.");
        }
        this.quantity = quantity;
    }
}
