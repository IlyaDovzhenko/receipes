package com.spring.recipes.services;

import com.spring.recipes.command.IngredientCommand;
import com.spring.recipes.domain.Ingredient;

import java.util.Set;

public interface IngredientService {
    Set<Ingredient> getIngredients();
    Ingredient findById(Long id);
    IngredientCommand findCommandById(Long id);
    IngredientCommand saveIngredientCommand(IngredientCommand ingredientCommand);
    void deleteById(Long id);
    IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId);
}
