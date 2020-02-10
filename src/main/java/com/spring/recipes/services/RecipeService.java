package com.spring.recipes.services;

import com.spring.recipes.command.RecipeCommand;
import com.spring.recipes.domain.Recipe;
import java.util.Set;

public interface RecipeService {
    Set<Recipe> getRecipes();
    Recipe findById(Long id);
    RecipeCommand findCommandById(Long id);
    RecipeCommand saveRecipeCommand(RecipeCommand recipeCommand);
}