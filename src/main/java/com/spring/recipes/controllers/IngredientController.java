package com.spring.recipes.controllers;

import com.spring.recipes.command.IngredientCommand;
import com.spring.recipes.services.IngredientService;
import com.spring.recipes.services.RecipeService;
import com.spring.recipes.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class IngredientController {

    private RecipeService recipeService;
    private IngredientService ingredientService;
    private UnitOfMeasureService uomService;

    public IngredientController(RecipeService recipeService,
                                IngredientService ingredientService,
                                UnitOfMeasureService uomService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.uomService = uomService;
        log.debug(IngredientController.class.getName() + "initialized! Dependencies are injected");
    }

    @GetMapping("recipes/{id}/ingredients")
    public String getIngredients(@PathVariable Long id, Model model) {
        model.addAttribute("recipe", recipeService.findCommandById(id));
        return "/recipes/ingredient/ingredients_list";
    }

    @GetMapping("recipes/{recipeId}/ingredients/{ingredientId}/show")
    public String showIngredient(@PathVariable Long recipeId,
                                 @PathVariable Long ingredientId,
                                 Model model) {
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId));
        return "recipes/ingredient/show";
    }

    @GetMapping("recipes/{recipeId}/ingredients/{ingredientId}/update")
    public String updateIngredient(@PathVariable Long recipeId,
                                   @PathVariable Long ingredientId,
                                   Model model) {
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId));
        model.addAttribute("uomList", uomService.getAll());
        return "/recipes/ingredient/ingredient_form";

    }

    @GetMapping("recipes/{recipeId}/ingredients/{ingredientId}/delete")
    public String deleteIngredient(@PathVariable Long recipeId,
                                   @PathVariable Long ingredientId) {
        ingredientService.deleteById(ingredientId);
        log.debug("Deleting ingredient with id:" + ingredientId);
        return "redirect:/recipes/" + recipeId + "/ingredients";
    }

    @PostMapping("/recipes/{recipeId}/ingredients")
    public String saveOrUpdate(@ModelAttribute IngredientCommand ingredientCommand) {
        IngredientCommand savedIngredientCommand = ingredientService.saveIngredientCommand(ingredientCommand);
        return "redirect:/recipes/" + ingredientCommand.getRecipeId() + "/ingredients/" + savedIngredientCommand.getId() + "/show";
    }
}
