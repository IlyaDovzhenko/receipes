package com.spring.recipes.services;

import com.spring.recipes.domain.Recipe;
import com.spring.recipes.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeServiceImpl recipeService;

    private Recipe returnedRecipe;
    private static final Long RECIPE_ID = 1L;
    private static final String RECIPE_DESCRIPTION = "recipe_description";

    @BeforeEach
    void setUp() {
        returnedRecipe = new Recipe();
        returnedRecipe.setId(RECIPE_ID);
        returnedRecipe.setDescription(RECIPE_DESCRIPTION);
    }

    @Test
    void getRecipes() {
        Recipe recipe = new Recipe();
        HashSet<Recipe> recipesData = new HashSet<>();
        recipesData.add(recipe);
        when(recipeRepository.findAll()).thenReturn(recipesData);

        Set<Recipe> recipes = recipeService.getRecipes();
        assertEquals(recipes.size(), 1);
        verify(recipeRepository, times(1)).findAll();
    }

    @Test
    void findByIdTest() {
        when(recipeRepository.findById(RECIPE_ID)).thenReturn(Optional.of(returnedRecipe));
        Recipe recipe = recipeService.findById(RECIPE_ID);

        assertNotNull(recipe);
        assertEquals(RECIPE_ID, recipe.getId());
        assertEquals(RECIPE_DESCRIPTION, recipe.getDescription());
        verify(recipeRepository, times(1)).findById(RECIPE_ID);

    }
}