package com.fii.picture.recipe.alignment.searchableencryption.service;

import java.util.List;

/**
 * Created by Ariana on 1/14/2019.
 */
public interface CrudService<RecipeDto> {
    List<RecipeDto> getAll(String id) throws Exception;
}
