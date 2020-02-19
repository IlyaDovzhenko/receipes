package com.spring.recipes.controllers;

import com.spring.recipes.services.IngredientService;
import com.spring.recipes.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@Controller
public class IngredientController {

    private RecipeService recipeService;
    private IngredientService ingredientService;

    public IngredientController(RecipeService recipeService,
                                IngredientService ingredientService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        log.debug(IngredientController.class.getName() + "initialized! Dependencies are injected");
    }

    @GetMapping("recipes/{id}/ingredients")
    public String getIngredients(@PathVariable Long id, Model model) {
        model.addAttribute("recipe", recipeService.findCommandById(id));
        return "/recipes/ingredient/ingredients_list";
    }

    @GetMapping("recipes/{recipeId}/ingredient/{ingredientId}/show")
    public String showIngredient(@PathVariable Long recipeId,
                                 @PathVariable Long ingredientId,
                                 Model model) {
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId));
        return "recipes/ingredient/show";
    }
}
