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
        this.recipeService = recipeService;
    }

    @GetMapping("/recipes")
    public String getRecipes(Model model) {
        model.addAttribute("recipes", recipeService.getRecipes());
        return "recipes_list";
    }

    @RequestMapping("/recipes/show/{id}")
    public String showById(@PathVariable Long id, Model model) {
        model.addAttribute("recipe", recipeService.findById(id));
        return "/recipes/show";
    }

    @RequestMapping("/recipes/new")
    public String newRecipe(Model model) {
        model.addAttribute("recipe", new RecipeCommand());
        return "recipes/recipe_form";
    }

    @PostMapping("/recipes")
    public String saveOrUpdate(@ModelAttribute RecipeCommand recipeCommand) {
        RecipeCommand savedRecipeCommand = recipeService.saveRecipeCommand(recipeCommand);
        return "redirect:/recipes/show/" + savedRecipeCommand.getId();
    }

}
