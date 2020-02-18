package com.spring.recipes.services;

import com.spring.recipes.command.RecipeCommand;
import com.spring.recipes.converters.RecipeCommandToRecipe;
import com.spring.recipes.converters.RecipeToRecipeCommand;
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

    @Mock
    private RecipeCommandToRecipe recipeCommandToRecipe;

    @Mock
    private RecipeToRecipeCommand recipeToRecipeCommand;

    @InjectMocks
    private RecipeServiceImpl recipeService;

    private Recipe returnedRecipe;
    private RecipeCommand recipeCommand;
    private static final Long RECIPE_ID = 3L;
    private static final String RECIPE_DESCRIPTION = "recipe_description";

    private static final String SAVE_EXCEPTION_MESSAGE = "Problem with convert recipe!";
    private static final String FIND_BY_ID_EXCEPTION_MESSAGE = "Recipe Not Found!";

    @BeforeEach
    void setUp() {
        returnedRecipe = new Recipe();
        returnedRecipe.setId(RECIPE_ID);
        returnedRecipe.setDescription(RECIPE_DESCRIPTION);

        recipeCommand = new RecipeCommand();
        recipeCommand.setId(RECIPE_ID);
        recipeCommand.setDescription(RECIPE_DESCRIPTION);
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

    @Test
    void findByIdExceptionTest() {
        when(recipeRepository.findById(RECIPE_ID)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> recipeService.findById(RECIPE_ID));

        assertEquals(FIND_BY_ID_EXCEPTION_MESSAGE, exception.getMessage());
        verify(recipeRepository, times(1)).findById(RECIPE_ID);
    }

    @Test
    void testSaveRecipeCommand() {
        when(recipeCommandToRecipe.convert(any())).thenReturn(returnedRecipe);
        when(recipeToRecipeCommand.convert(any())).thenReturn(recipeCommand);

        RecipeCommand recipeCommandToSave = recipeService.saveRecipeCommand(recipeCommand);
        assertNotNull(recipeCommandToSave);
        assertEquals(RECIPE_ID, recipeCommandToSave.getId());
        assertEquals(RECIPE_DESCRIPTION, recipeCommandToSave.getDescription());
        verify(recipeRepository, times(1)).save(any());
    }

    @Test
    void saveExceptionTest() {
        when(recipeCommandToRecipe.convert(any())).thenReturn(null);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> recipeService.saveRecipeCommand(recipeCommand));
        assertEquals(SAVE_EXCEPTION_MESSAGE, exception.getMessage());
    }

    @Test
    void testFindCommandById() {
        when(recipeRepository.findById(any())).thenReturn(Optional.of(returnedRecipe));
        when(recipeToRecipeCommand.convert(any())).thenReturn(recipeCommand);

        RecipeCommand newRecipeCommand = recipeService.findCommandById(1L);
        assertNotNull(newRecipeCommand);
        assertEquals(RECIPE_ID, newRecipeCommand.getId());
        assertEquals(RECIPE_DESCRIPTION, newRecipeCommand.getDescription());
        verify(recipeToRecipeCommand, times(1)).convert(any());
        verify(recipeRepository, times(1)).findById(any());
    }

    @Test
    void testDeleteById() {
        // given

        //when
        recipeService.deleteById(RECIPE_ID);

        //then
        verify(recipeRepository, times(1)).deleteById(any());
    }
}