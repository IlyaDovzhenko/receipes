package com.spring.recipes.services;

import com.spring.recipes.domain.Recipe;
import java.util.Set;

public interface RecipeService {
    Set<Recipe> getRecipes();
}
