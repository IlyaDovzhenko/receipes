package com.spring.recipes.controllers;

import com.spring.recipes.command.IngredientCommand;
import com.spring.recipes.command.RecipeCommand;
import com.spring.recipes.domain.Ingredient;
import com.spring.recipes.domain.Recipe;
import com.spring.recipes.domain.UnitOfMeasure;
import com.spring.recipes.services.IngredientService;
import com.spring.recipes.services.RecipeService;
import com.spring.recipes.services.UnitOfMeasureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class IngredientControllerTest {

    @Mock
    private RecipeService recipeService;
    @Mock
    private IngredientService ingredientService;
    @Mock
    private UnitOfMeasureService uomService;
    @Mock
    private Model model;

    @InjectMocks
    private IngredientController ingredientController;

    private MockMvc mockMvc;

    private Recipe returnedRecipe;
    private RecipeCommand returnedRecipeCommand;
    private final Long RECIPE_ID = 1L;
    private final String RECIPE_DESCRIPTION = "recipeCommand";

    private Ingredient returnedIngredient;
    private IngredientCommand returnedIngredientCommand;
    private final Long INGREDIENT_ID = 2L;
    private final String INGREDIENT_DESCRIPTION = "ingredient";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ingredientController).build();

        returnedRecipe = new Recipe();
        returnedRecipe.setId(RECIPE_ID);
        returnedRecipe.setDescription(RECIPE_DESCRIPTION);

        returnedRecipeCommand = new RecipeCommand();
        returnedRecipeCommand.setId(RECIPE_ID);
        returnedRecipeCommand.setDescription(RECIPE_DESCRIPTION);

        returnedIngredient = new Ingredient();
        returnedIngredient.setId(INGREDIENT_ID);
        returnedIngredient.setDescription(INGREDIENT_DESCRIPTION);
        returnedIngredient.setRecipe(returnedRecipe);

        returnedIngredientCommand = new IngredientCommand();
        returnedIngredientCommand.setId(INGREDIENT_ID);
        returnedIngredientCommand.setDescription(INGREDIENT_DESCRIPTION);
        returnedIngredientCommand.setRecipeId(RECIPE_ID);

        returnedRecipe.getIngredients().add(returnedIngredient);
        returnedRecipeCommand.getIngredients().add(returnedIngredientCommand);

    }

    @Test
    void getIngredientsTest() throws Exception {
        //when
        when(recipeService.findCommandById(anyLong())).thenReturn(returnedRecipeCommand);

        //then
        mockMvc.perform(get("/recipes/" + RECIPE_ID + "/ingredients"))
                .andExpect(status().isOk())
                .andExpect(view().name("/recipes/ingredient/ingredients_list"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    void showIngredient() throws Exception {
        //when
        when(ingredientService.findByRecipeIdAndIngredientId(anyLong(), anyLong()))
                .thenReturn(returnedIngredientCommand);

        //then
        mockMvc.perform(get("/recipes/" + RECIPE_ID + "/ingredients/" + INGREDIENT_ID + "/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipes/ingredient/show"))
                .andExpect(model().attributeExists("ingredient"));
    }

    @Test
    void updateIngredient() throws Exception {
        //given
        Set<UnitOfMeasure> uomList = new HashSet<>();
        uomList.add(new UnitOfMeasure());

        //when
        when(ingredientService.findByRecipeIdAndIngredientId(anyLong(), anyLong()))
                .thenReturn(returnedIngredientCommand);
        when(uomService.getAll()).thenReturn(uomList);

        //then
        mockMvc.perform(get("/recipes/" + RECIPE_ID + "/ingredients/" + INGREDIENT_ID + "/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("/recipes/ingredient/ingredient_form"))
                .andExpect(model().attributeExists("ingredient", "uomList"));
    }

    @Test
    void deleteIngredient() throws Exception {
        //then
        mockMvc.perform(get("/recipes/" + RECIPE_ID + "/ingredients/" + INGREDIENT_ID + "/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipes/" + RECIPE_ID + "/ingredients"));
        verify(ingredientService, times(1)).deleteById(anyLong());
    }

    @Test
    void saveOrUpdate() {

    }
}