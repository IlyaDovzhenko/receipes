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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeToRecipeCommandTest {

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
    private IngredientToIngredientCommand ingredientConverter;

    @Mock
    private NotesToNotesCommand notesConverter;

    @Mock
    private CategoryToCategoryCommand categoryConverter;

    @InjectMocks
    private RecipeToRecipeCommand recipeConverter;

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
        assertNull(recipeConverter.convert(null));
    }

    @Test
    void testEmptyObject() {
        assertNotNull(recipeConverter.convert(new Recipe()));
    }

    @Test
    void convert() {
        //given
        when(ingredientConverter.convert(ingredient)).thenReturn(ingredientCommand);
        when(notesConverter.convert(notes)).thenReturn(notesCommand);
        when(categoryConverter.convert(category)).thenReturn(categoryCommand);

        Recipe recipe = new Recipe();
        recipe.setId(RECIPE_ID);
        recipe.setDescription(RECIPE_DESCRIPTION);
        recipe.setPrepTime(RECIPE_PREP_TIME);
        recipe.setCookTime(RECIPE_COOK_TIME);
        recipe.setServings(RECIPE_SERVINGS);
        recipe.setUrl(RECIPE_URL);
        recipe.setDirections(RECIPE_DIRECTIONS);
        recipe.setDifficulty(RECIPE_DIFFICULTY);
        recipe.getIngredients().add(ingredient);
        recipe.setImage(null);
        recipe.setNotes(notes);
        recipe.getCategories().add(category);

        //when
        RecipeCommand recipeCommand = recipeConverter.convert(recipe);

        //then
        assertNotNull(recipeCommand);
        assertEquals(RECIPE_ID, recipeCommand.getId());
        assertEquals(RECIPE_DESCRIPTION, recipeCommand.getDescription());
        assertEquals(RECIPE_PREP_TIME, recipeCommand.getPrepTime());
        assertEquals(RECIPE_COOK_TIME, recipeCommand.getCookTime());
        assertEquals(RECIPE_SERVINGS, recipeCommand.getServings());
        assertEquals(RECIPE_URL, recipeCommand.getUrl());
        assertEquals(RECIPE_DIRECTIONS, recipeCommand.getDirections());
        assertEquals(RECIPE_DIFFICULTY, recipeCommand.getDifficulty());
        assertEquals(1, recipeCommand.getIngredients().size());
        assertNull(recipeCommand.getImage());
        assertEquals(NOTES_ID, recipeCommand.getNotes().getId());
        assertEquals(NOTES, recipeCommand.getNotes().getRecipeNotes());
        assertEquals(1, recipeCommand.getCategories().size());

        verify(ingredientConverter, times(1)).convert(ingredient);
        verify(notesConverter, times(1)).convert(notes);
        verify(categoryConverter, times(1)).convert(category);
    }
}