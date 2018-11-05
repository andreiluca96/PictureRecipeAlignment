package com.example.lucaandrei.picturerecipealignment.model;

import junit.framework.Assert;

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

    @Test
    public void Given_EqualObjects_When_IngredientNameAndQuantityEquals_Then_ReturnTrue() {
        // Arrange
        String ingredientName  = "Ingredient";
        int ingredientQuantity = 1;

        Ingredient ingredient1 = new Ingredient();
        Ingredient ingredient2 = new Ingredient();

        ingredient1.setName(ingredientName);
        ingredient2.setName(ingredientName);

        ingredient1.setQuantity(ingredientQuantity);
        ingredient2.setQuantity(ingredientQuantity);

        Assert.assertTrue(ingredient1.equals(ingredient2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void Given_EmptyName_When_SetIngredientName_Then_ThrowInvalidArgumentException() {
        String emptyString = "";

        Ingredient ingredient = new Ingredient();
        ingredient.setName(emptyString);
    }
}
