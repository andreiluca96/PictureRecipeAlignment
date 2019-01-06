package com.example.lucaandrei.picturerecipealignment.model;

import java.util.Objects;

public class Ingredient {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        Objects.requireNonNull(name);
        if (name.isEmpty()){
           throw new IllegalArgumentException("The name value must not be empty.");
        }

        this.name = name;
    }
}
