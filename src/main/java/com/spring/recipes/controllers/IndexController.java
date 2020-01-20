package com.spring.recipes.controllers;

import com.spring.recipes.domain.Category;
import com.spring.recipes.domain.UnitOfMeasure;
import com.spring.recipes.repositories.CategoryRepository;
import com.spring.recipes.repositories.UnitOfMeasureRepository;
import com.spring.recipes.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Slf4j
@Controller
public class IndexController {

    private CategoryRepository categoryRepository;
    private UnitOfMeasureRepository unitOfMeasureRepository;
    private RecipeService recipeService;

    public IndexController(CategoryRepository categoryRepository,
                           UnitOfMeasureRepository unitOfMeasureRepository,
                           RecipeService recipeService) {
        this.categoryRepository = categoryRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.recipeService = recipeService;
        log.debug("Dependencies are injected!");
    }

    @GetMapping({"", "/", "/index", "/index.html"})
    public String getIndexPage() {

        Optional<Category> categoryOptional = categoryRepository.findByDescription("American");
        Optional<UnitOfMeasure> unitOfMeasureOptional = unitOfMeasureRepository.findByDescription("Teaspoon");

        System.out.println(categoryOptional.get().getId() + " - " + categoryOptional.get().getDescription());
        System.out.println(unitOfMeasureOptional.get().getId() + " - " + unitOfMeasureOptional.get().getDescription());

        Iterable<Category> categories = categoryRepository.findAll();
        for (Category category : categories) {
            System.out.println(category.getDescription());
        }
        return "index";
    }

    @GetMapping("/recipes")
    public String getRecipes(Model model) {
        model.addAttribute("recipes", recipeService.getRecipes());
        return "recipes_list";
    }

    @RequestMapping("/recipes/show/{id}")
    public String showById(@PathVariable Long id, Model model) {
        model.addAttribute("recipe", recipeService.findById(id));
        //model.addAttribute("categories", recipeService.findById(id).getCategories());
        return "/recipes/show";
    }
}