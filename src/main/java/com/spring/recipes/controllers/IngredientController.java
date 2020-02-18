package com.spring.recipes.controllers;

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

    public IngredientController(RecipeService recipeService) {
        log.debug(IngredientController.class.getName() + "initialized!");
        this.recipeService = recipeService;
    }

    @GetMapping("recipes/{id}/ingredients")
    public String getIngredients(@PathVariable Long id, Model model) {
        model.addAttribute("recipe", recipeService.findCommandById(id));
        return "/recipes/ingredient/ingredients_list";
    }
}
