package com.fii.picture.recipe.alignment.searchableencryption.Dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ariana on 1/14/2019.
 */
public class RecipeDto {
    public List<String> ingredients = new ArrayList<String>();

    public RecipeDto(List<String> ingredients) {
        ingredients = ingredients;
    }
}
