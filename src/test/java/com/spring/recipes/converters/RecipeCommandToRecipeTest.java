package com.spring.recipes.converters;

import com.spring.recipes.command.CategoryCommand;
import com.spring.recipes.command.IngredientCommand;
import com.spring.recipes.command.NotesCommand;
import com.spring.recipes.command.RecipeCommand;
import com.spring.recipes.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
    private static final Difficulty RECIPE_DIFFICULTY = Difficulty.MODERATE;

    private Ingredient ingredient;
    private IngredientCommand ingredientCommand;
    private static final Long INGREDIENT_ID = 5L;
    private static final String INGREDIENT_DESCRIPTION = "ingredient_description";

    private Notes notes;
    private NotesCommand notesCommand;
    private static final Long NOTES_ID = 6L;
    private static final String NOTES = "recipe_notes";

    private Category category;
    private CategoryCommand categoryCommand;
    private static final Long CATEGORY_ID = 7L;
    private static final String CATEGORY_DESCRIPTION = "category_description";

    @Mock
    private IngredientCommandToIngredient ingredientConverter;

    @Mock
    private NotesCommandToNotes notesConverter;

    @Mock
    private CategoryCommandToCategory categoryConverter;

    @InjectMocks
    private RecipeCommandToRecipe recipeCommandConverter;

    @BeforeEach
    void setUp() {
        ingredient = new Ingredient();
        ingredient.setId(INGREDIENT_ID);
        ingredient.setDescription(INGREDIENT_DESCRIPTION);

        ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(INGREDIENT_ID);
        ingredientCommand.setDescription(INGREDIENT_DESCRIPTION);

        notes = new Notes();
        notes.setId(NOTES_ID);
        notes.setRecipeNotes(NOTES);

        notesCommand = new NotesCommand();
        notesCommand.setId(NOTES_ID);
        notesCommand.setRecipeNotes(NOTES);

        category = new Category();
        category.setId(CATEGORY_ID);
        category.setDescription(CATEGORY_DESCRIPTION);

        categoryCommand = new CategoryCommand();
        categoryCommand.setId(CATEGORY_ID);
        categoryCommand.setDescription(CATEGORY_DESCRIPTION);
    }

    @Test
    void testNullObject() {
        assertNull(recipeCommandConverter.convert(null));
    }

    @Test
    void testEmptyObject() {
        assertNotNull(recipeCommandConverter.convert(new RecipeCommand()));
    }

    @Test
    void convert() {
        //given


        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(RECIPE_ID);
        recipeCommand.setDescription(RECIPE_DESCRIPTION);
        recipeCommand.setPrepTime(RECIPE_PREP_TIME);
        recipeCommand.setCookTime(RECIPE_COOK_TIME);
        recipeCommand.setServings(RECIPE_SERVINGS);
        recipeCommand.setUrl(RECIPE_URL);
        recipeCommand.setDirections(RECIPE_DIRECTIONS);
        recipeCommand.setDifficulty(RECIPE_DIFFICULTY);

    }
}