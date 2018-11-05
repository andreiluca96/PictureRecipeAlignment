package com.example.lucaandrei.picturerecipealignment.model;

import org.junit.Test;

public class IngredientTest {
    @Test(expected = IllegalArgumentException.class)
    public void Given_NegativeQuantity_When_SetIngredientQuantity_Then_ThrowInvalidArgumentException() {
        int negativeQuantity = -100;

        Ingredient ingredient = new Ingredient();
        ingredient.setQuantity(negativeQuantity);
    }

    @Test(expected = NullPointerException.class)
    public void Given_NullName_When_SetIngredientName_Then_ThrowNullPointerException() {
        String nullName = null;

        Ingredient ingredient = new Ingredient();
        ingredient.setName(nullName);
    }
}
