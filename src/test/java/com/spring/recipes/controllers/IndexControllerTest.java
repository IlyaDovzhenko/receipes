package com.spring.recipes.controllers;

import com.spring.recipes.repositories.CategoryRepository;
import com.spring.recipes.repositories.UnitOfMeasureRepository;
import com.spring.recipes.services.RecipeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;

class IndexControllerTest {

    private IndexController indexController;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;

    @Mock
    RecipeServiceImpl recipeService;

    @Mock
    Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        indexController = new IndexController(categoryRepository, unitOfMeasureRepository, recipeService);
    }

    @Test
    void getIndexPage() {

    }

    @Test
    void getRecipes() {
        assertEquals("recipes_list", indexController.getRecipes(model));
        Mockito.verify(recipeService, Mockito.times(1)).getRecipes();
    }
}