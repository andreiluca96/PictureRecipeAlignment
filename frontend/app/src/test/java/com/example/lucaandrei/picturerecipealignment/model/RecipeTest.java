package com.example.lucaandrei.picturerecipealignment.model;

import org.junit.Test;

public class RecipeTest {

    @Test(expected = IllegalArgumentException.class)
    public void Given_IllegalUrl_When_SetRecipeUrl_Then_ThrowInvalidArgumentException() {
        String invalidUrl = "invalid.url.com";

        Recipe recipe = new Recipe();
        recipe.setUrl(invalidUrl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void Given_IllegalStrip_When_SetRecipeStrip_Then_ThrowInvalidArgumentException() {
        String invalidStrip = "test_strip";

        Recipe recipe = new Recipe();
        recipe.setStrip(invalidStrip);
    }
}