package com.spring.recipes.controllers;

import com.spring.recipes.command.RecipeCommand;
import com.spring.recipes.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class RecipeController {

    private RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        log.debug(RecipeController.class.getName() + " initialized!");
        this.recipeService = recipeService;
    }

    @GetMapping("/recipes")
    public String getRecipes(Model model) {
        model.addAttribute("recipes", recipeService.getRecipes());
        return "recipes_list";
    }

    @GetMapping("/recipes/{id}/show")
    public String showById(@PathVariable Long id, Model model) {
        model.addAttribute("recipe", recipeService.findById(id));
        return "/recipes/show";
    }

    @GetMapping("/recipes/{id}/update")
    public String updateById(@PathVariable Long id, Model model) {
        model.addAttribute("recipe", recipeService.findCommandById(id));
        return "/recipes/recipe_form";
    }

    @GetMapping("/recipes/new")
    public String newRecipe(Model model) {
        model.addAttribute("recipe", new RecipeCommand());
        return "/recipes/recipe_form";
    }

    @GetMapping("/recipes/{id}/delete")
    public String deleteById(@PathVariable Long id) {
        log.debug("Deleting recipe: " + id);
        recipeService.deleteById(id);
        return "redirect:/recipes";
    }

    @PostMapping("recipes")
    public String saveOrUpdate(@ModelAttribute RecipeCommand recipeCommand) {
        RecipeCommand savedRecipeCommand = recipeService.saveRecipeCommand(recipeCommand);
        return "redirect:/recipes/" + savedRecipeCommand.getId() + "/show";
    }
}