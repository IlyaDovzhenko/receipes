package com.spring.recipes.services;

import com.spring.recipes.command.IngredientCommand;
import com.spring.recipes.command.UnitOfMeasureCommand;
import com.spring.recipes.converters.IngredientCommandToIngredient;
import com.spring.recipes.converters.IngredientToIngredientCommand;
import com.spring.recipes.domain.Ingredient;
import com.spring.recipes.domain.Recipe;
import com.spring.recipes.domain.UnitOfMeasure;
import com.spring.recipes.repositories.IngredientRepository;
import com.spring.recipes.repositories.RecipeRepository;
import com.spring.recipes.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@ExtendWith(MockitoExtension.class)
class IngredientServiceImplTest {

    @Mock
    private IngredientRepository ingredientRepository;
    @Mock
    private RecipeService recipeService;
    @Mock
    private RecipeRepository recipeRepository;
    @Mock
    private UnitOfMeasureRepository uomRepository;
    @Mock
    private IngredientToIngredientCommand ingredientToCommand;
    @Mock
    private IngredientCommandToIngredient commandToIngredient;

    @InjectMocks
    private IngredientServiceImpl ingredientService;

    private Ingredient returnedIngredient;
    private IngredientCommand returnedIngredientCommand;
    private final Long INGREDIENT_ID = 1L;
    private final String INGREDIENT_DESCRIPTION = "ingredient";
    private Recipe returnedRecipe;
    private UnitOfMeasureCommand returnedUomCommand;
    private UnitOfMeasure returnedUom;

    @BeforeEach
    void setUp() {
        returnedIngredient = new Ingredient();
        returnedIngredient.setId(INGREDIENT_ID);
        returnedIngredient.setDescription(INGREDIENT_DESCRIPTION);

        returnedIngredientCommand = new IngredientCommand();
        returnedIngredientCommand.setId(INGREDIENT_ID);
        returnedIngredientCommand.setDescription(INGREDIENT_DESCRIPTION);

        returnedRecipe = new Recipe();
        returnedRecipe.setId(1L);
        returnedRecipe.setDescription("testRecipe");

        returnedUomCommand = new UnitOfMeasureCommand();
        returnedUomCommand.setId(1L);
        returnedUomCommand.setDescription("uom");

        returnedUom = new UnitOfMeasure();
        returnedUom.setId(1L);
        returnedUom.setDescription("uom");
    }

    @Test
    void getIngredientsTest() {
        //given
        Set<Ingredient> returnedIngredients = new HashSet<>();
        returnedIngredients.add(returnedIngredient);

        //when
        when(ingredientRepository.findAll()).thenReturn(returnedIngredients);
        Set<Ingredient> ingredients = ingredientService.getIngredients();

        //then
        assertNotNull(ingredients);
        assertEquals(1, ingredients.size());
        verify(ingredientRepository, times(1)).findAll();
    }

    @Test
    void findByIdTest() {
        //when
        when(ingredientRepository.findById(anyLong())).thenReturn(Optional.of(returnedIngredient));
        Ingredient ingredient = ingredientService.findById(INGREDIENT_ID);

        //then
        assertNotNull(ingredient);
        assertEquals(INGREDIENT_ID, ingredient.getId());
        assertEquals(INGREDIENT_DESCRIPTION, ingredient.getDescription());
        verify(ingredientRepository, times(1)).findById(anyLong());
    }

    @Test
    void findByIdExceptionTest() {
        //given
        String exMessage = "Ingredient with id:" + INGREDIENT_ID + " not found!";

        //when
        when(ingredientRepository.findById(INGREDIENT_ID)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> ingredientService.findById(INGREDIENT_ID));

        //then
        assertNotNull(exception);
        assertEquals(exMessage, exception.getMessage());
        verify(ingredientRepository, times(1)).findById(INGREDIENT_ID);
    }

    @Test
    void findCommandByIdTest() {
        //when
        when(ingredientRepository.findById(INGREDIENT_ID)).thenReturn(Optional.of(returnedIngredient));
        when(ingredientToCommand.convert(any())).thenReturn(returnedIngredientCommand);
        IngredientCommand ingredientCommand = ingredientService.findCommandById(INGREDIENT_ID);

        //then
        assertNotNull(ingredientCommand);
        assertEquals(INGREDIENT_ID, ingredientCommand.getId());
        assertEquals(INGREDIENT_DESCRIPTION, ingredientCommand.getDescription());
        verify(ingredientRepository, times(1)).findById(INGREDIENT_ID);
        verify(ingredientToCommand, times(1)).convert(any());
    }

    @Test
    void saveIngredientCommandTest() {
        //given
        returnedIngredientCommand.setRecipeId(1L);
        returnedIngredientCommand.setUnitOfMeasure(returnedUomCommand);

        returnedRecipe.getIngredients().add(returnedIngredient);

        //when
        when(recipeService.findById(anyLong())).thenReturn(returnedRecipe);
        when(uomRepository.findById(anyLong())).thenReturn(Optional.of(returnedUom));
        when(recipeRepository.save(any())).thenReturn(returnedRecipe);
        when(ingredientToCommand.convert(any())).thenReturn(returnedIngredientCommand);

        IngredientCommand savedIngredientCommand = ingredientService.saveIngredientCommand(returnedIngredientCommand);

        //then
        assertNotNull(savedIngredientCommand);
        assertEquals(INGREDIENT_ID, savedIngredientCommand.getId());
        assertEquals(INGREDIENT_DESCRIPTION, savedIngredientCommand.getDescription());
        assertEquals(returnedUom.getId(), savedIngredientCommand.getUnitOfMeasure().getId());
        assertEquals(returnedUom.getDescription(), savedIngredientCommand.getUnitOfMeasure().getDescription());
        verify(recipeRepository, times(1)).save(any());
        verify(recipeService, times(1)).findById(anyLong());
        verify(uomRepository, times(1)).findById(anyLong());
        verify(ingredientToCommand, times(1)).convert(any());
    }

    @Test
    void saveIngredientCommandNotExistsUomExceptionTest() {
        //given
        String exMessage = "Unit of Measure not found!";
        returnedIngredientCommand.setRecipeId(1L);
        returnedIngredientCommand.setUnitOfMeasure(returnedUomCommand);

        returnedRecipe.getIngredients().add(returnedIngredient);

        //when
        when(recipeService.findById(anyLong())).thenReturn(returnedRecipe);
        when(uomRepository.findById(anyLong())).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> ingredientService.saveIngredientCommand(returnedIngredientCommand));

        //then
        assertNotNull(exception);
        assertEquals(exMessage, exception.getMessage());
    }

    @Test
    void saveIngredientCommandEmptyIngredient() {
        //given
        String exMessage = "No ingredient to save!";
        returnedIngredientCommand.setRecipeId(1L);

        //when
        when(recipeService.findById(anyLong())).thenReturn(returnedRecipe);
        when(commandToIngredient.convert(any())).thenReturn(null);
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> ingredientService.saveIngredientCommand(returnedIngredientCommand));

        //then
        assertNotNull(exception);
        assertEquals(exMessage, exception.getMessage());
    }

    @Test
    void saveIngredientCommandNotEmptyIngredient() {
        //given
        String exMessage = "No ingredient to save!";
        returnedIngredientCommand.setRecipeId(1L);

        //when
        when(recipeService.findById(anyLong())).thenReturn(returnedRecipe);
        when(commandToIngredient.convert(any())).thenReturn(returnedIngredient);
        when(recipeRepository.save(any())).thenReturn(returnedRecipe);
        when(ingredientToCommand.convert(any())).thenReturn(returnedIngredientCommand);
        IngredientCommand savedIngredientCommand = ingredientService.saveIngredientCommand(returnedIngredientCommand);

        //then
        assertNotNull(savedIngredientCommand);
    }

    @Test
    void saveIngredientCommandNotSaveIngredientToRecipe() {
        //given
        String exMessage = "Ingredient not saved!";
        returnedIngredientCommand.setRecipeId(1L);

        Recipe recipeWithoutIngredient = new Recipe();
        recipeWithoutIngredient.setId(2L);
        recipeWithoutIngredient.setDescription("recipeWithoutIngredient");

        //when
        when(recipeService.findById(anyLong())).thenReturn(returnedRecipe);
        when(commandToIngredient.convert(any())).thenReturn(returnedIngredient);
        when(recipeRepository.save(any())).thenReturn(recipeWithoutIngredient);
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> ingredientService.saveIngredientCommand(returnedIngredientCommand));

        //then
        assertNotNull(exception);
        assertEquals(exMessage, exception.getMessage());
    }

    @Test
    void deleteById() {
        //when
        ingredientService.deleteById(INGREDIENT_ID);

        //then
        verify(ingredientRepository, times(1)).deleteById(any());
    }

    @Test
    void findByRecipeIdAndIngredientIdTest() {
        //given
        Recipe recipe = new Recipe();
        recipe.setId(3L);
        recipe.setDescription("recipe_3L");
        recipe.addIngredient(returnedIngredient);

        //when
        when(recipeService.findById(anyLong())).thenReturn(recipe);
        when(ingredientToCommand.convert(any())).thenReturn(returnedIngredientCommand);
        IngredientCommand ingredientCommand =
                ingredientService.findByRecipeIdAndIngredientId(3L, 1L);

        //then
        assertNotNull(ingredientCommand);
        assertEquals(INGREDIENT_ID, ingredientCommand.getId());
        assertEquals(INGREDIENT_DESCRIPTION, ingredientCommand.getDescription());
        verify(recipeService, times(1)).findById(anyLong());
        verify(ingredientToCommand, times(1)).convert(any());
    }

    @Test
    void findByRecipeIdAndIngredientIdRecipeExceptionTest() {
        //given
        String exMessage = "Can't find recipe with id:";
        Long recipeId = 1L;
        Long ingredientId = 1L;

        //when
        when(recipeService.findById(any())).thenReturn(null);
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId));

        //then
        assertNotNull(exception);
        assertEquals(exMessage + recipeId, exception.getMessage());
    }

    @Test
    void findByRecipeIdAndIngredientIdIngredientExceptionTest() {
        //given
        String exMessage = "Can't find ingredient with id:";
        Long recipeId = 1L;
        Long ingredientId = 1L;
        Recipe recipe = new Recipe();
        recipe.setId(recipeId);
        recipe.setDescription("recipe_3L");

        //when
        when(recipeService.findById(recipeId)).thenReturn(recipe);
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId));

        //then
        assertNotNull(exception);
        assertEquals(exMessage + ingredientId, exception.getMessage());
    }
}