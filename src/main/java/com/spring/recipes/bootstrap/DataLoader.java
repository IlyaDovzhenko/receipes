package com.spring.recipes.bootstrap;

import com.spring.recipes.domain.*;
import com.spring.recipes.repositories.CategoryRepository;
import com.spring.recipes.repositories.RecipeRepository;
import com.spring.recipes.repositories.UnitOfMeasureRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private RecipeRepository recipeRepository;
    private CategoryRepository categoryRepository;
    private UnitOfMeasureRepository unitOfMeasureRepository;

    public DataLoader(RecipeRepository recipeRepository,
                      CategoryRepository categoryRepository,
                      UnitOfMeasureRepository unitOfMeasureRepository) {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        recipeRepository.saveAll(getRecipes());
        System.out.println("Data is created and save!");
    }

    public List<Recipe> getRecipes() {

        List<Recipe> recipes = new ArrayList<>();

        UnitOfMeasure eachUom = findUnitOfMeasure("Each");
        UnitOfMeasure teaspoonUom = findUnitOfMeasure("Teaspoon");
        UnitOfMeasure tablespoonUom = findUnitOfMeasure("Tablespoon");
        UnitOfMeasure dashUom = findUnitOfMeasure("Dash");
        UnitOfMeasure pintUom = findUnitOfMeasure("Pint");
        UnitOfMeasure cupUom = findUnitOfMeasure("Cup");
        UnitOfMeasure thingUom = findUnitOfMeasure("Thing");

        Category americanCategory = findCategory("American");
        Category italianCategory = findCategory("Italian");
        Category mexicanCategory = findCategory("Mexican");
        Category fastFoodCategory = findCategory("Fast Food");

        Recipe guacamoleRecipe = new Recipe();
        guacamoleRecipe.setDescription("Perfect Guacamole");

        Notes notes = new Notes();
        notes.setRecipeNotes("It is good food!");

        guacamoleRecipe.getCategories().add(americanCategory);
        guacamoleRecipe.getCategories().add(mexicanCategory);
        guacamoleRecipe.setDifficulty(Difficulty.EASY);
        guacamoleRecipe.setPrepTime(20);
        guacamoleRecipe.setCookTime(60);
        guacamoleRecipe.setServings(24);
        guacamoleRecipe.setUrl("uuuuurl");
        guacamoleRecipe.setDirections("Cook!");
        guacamoleRecipe.setNotes(notes);

        guacamoleRecipe.addIngredient(new Ingredient("ripe avocado", new BigDecimal(1), thingUom));
        guacamoleRecipe.addIngredient(new Ingredient("Kosher salt", new BigDecimal(0.5), teaspoonUom));
        guacamoleRecipe.addIngredient(new Ingredient("of fresh lime juice or lemon juice", new BigDecimal(1), tablespoonUom));
        guacamoleRecipe.addIngredient(new Ingredient("to 1/4 cup of minced red onion or thinly sliced green onion", new BigDecimal(1), tablespoonUom));
        guacamoleRecipe.addIngredient(new Ingredient("serrano chiles, stems and seeds removed, minced", new BigDecimal(1), thingUom));
        guacamoleRecipe.addIngredient(new Ingredient("cilantro (leaves and tender stems), finely chopped", new BigDecimal(2), tablespoonUom));
        guacamoleRecipe.addIngredient(new Ingredient("of freshly grated black pepper", new BigDecimal(0), dashUom));
        guacamoleRecipe.addIngredient(new Ingredient("ripe tomato, seeds and pulp removed, chopped", new BigDecimal(0.5), thingUom));

        recipes.add(guacamoleRecipe);

        System.out.println("Creating data...");
        return recipes;
    }

    private UnitOfMeasure findUnitOfMeasure(String description) {
        Optional<UnitOfMeasure> unitOfMeasureOptional = unitOfMeasureRepository.findByDescription(description);
        if (!unitOfMeasureOptional.isPresent()) {
            throw new RuntimeException("Expected UOM not found!");
        }
        return unitOfMeasureOptional.get();
    }

    private Category findCategory(String description) {
        Optional<Category> category = categoryRepository.findByDescription(description);
        if (!category.isPresent()) {
            throw new RuntimeException("Expected category not found!");
        }
        return category.get();
    }
}
