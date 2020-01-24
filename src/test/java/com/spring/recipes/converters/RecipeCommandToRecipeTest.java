package com.spring.recipes.converters;

import com.spring.recipes.domain.Difficulty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RecipeCommandToRecipeTest {

    private static final Long RECIPE_ID = 1L;
    private static final String RECIPE_DESCRIPTION = "recipe_description";
    private static final Integer RECIPE_PREP_TIME = 1;
    private static final Integer RECIPE_COOK_TIME = 2;
    private static final Integer RECIPE_SERVINGS = 3;
    private static final String RECIPE_URL = "recipe_url";
    private static final String RECIPE_DIRECTIONS = "recipe_directions";
    private static final Difficulty difficulty = Difficulty.MODERATE;

    @BeforeEach
    void setUp() {
    }

    @Test
    void convert() {
    }
}