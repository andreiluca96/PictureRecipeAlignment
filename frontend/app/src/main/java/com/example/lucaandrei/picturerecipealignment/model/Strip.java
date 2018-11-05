package com.example.lucaandrei.picturerecipealignment.model;

public enum Strip {
    TRAIN("train"), VALIDATION("valid"), TEST("test");
    private String value;

    Strip(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
