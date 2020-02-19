package com.spring.recipes.services;

import com.spring.recipes.command.IngredientCommand;
import com.spring.recipes.command.RecipeCommand;
import com.spring.recipes.converters.IngredientCommandToIngredient;
import com.spring.recipes.converters.IngredientToIngredientCommand;
import com.spring.recipes.domain.Ingredient;
import com.spring.recipes.domain.Recipe;
import com.spring.recipes.repositories.IngredientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private IngredientRepository ingredientRepository;
    private RecipeService recipeService;
    private IngredientToIngredientCommand ingredientToCommand;
    private IngredientCommandToIngredient commandToIngredient;

    public IngredientServiceImpl(IngredientRepository ingredientRepository,
                                 RecipeService recipeService,
                                 IngredientToIngredientCommand ingredientToCommand,
                                 IngredientCommandToIngredient commandToIngredient) {
        this.ingredientRepository = ingredientRepository;
        this.recipeService = recipeService;
        this.ingredientToCommand = ingredientToCommand;
        this.commandToIngredient = commandToIngredient;
        log.debug("Service initialized! Dependencies are injected!");
    }

    @Override
    public Set<Ingredient> getIngredients() {
        Set<Ingredient> ingredients = new HashSet<>();
        ingredientRepository.findAll().forEach(ingredients::add);
        return ingredients;
    }

    @Override
    public Ingredient findById(Long id) {
        Optional<Ingredient> optionalIngredient = ingredientRepository.findById(id);
        if (!optionalIngredient.isPresent()) {
            throw new RuntimeException("Ingredient with id:" + id + " not found!");
        }
        return optionalIngredient.get();
    }

    @Override
    @Transactional
    public IngredientCommand findCommandById(Long id) {
        return ingredientToCommand.convert(findById(id));
    }

    @Override
    @Transactional
    public IngredientCommand saveIngredientCommand(IngredientCommand ingredientCommand) {
        Ingredient ingredientToSave = commandToIngredient.convert(ingredientCommand);
        if (ingredientToSave != null) {
            ingredientRepository.save(ingredientToSave);
            log.debug("Saved ingredient with id:" + ingredientToSave.getId());
            return ingredientToCommand.convert(ingredientToSave);
        } else {
            throw new RuntimeException("No ingredient to save!");
        }
    }

    @Override
    public void deleteById(Long id) {
        ingredientRepository.deleteById(id);
        log.debug("Ingredient with id:" + id + "deleted!");
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {
        Recipe recipe = recipeService.findById(recipeId);
        Optional<Ingredient> optionalIngredient;
        if (recipe != null) {
            optionalIngredient = recipe.getIngredients().stream()
                    .filter(ingredient -> ingredient.getId().equals(ingredientId))
                    .findFirst();
        } else {
            throw new RuntimeException("Can't find recipe with id:" + recipeId);
        }
        if (!optionalIngredient.isPresent()) {
            throw new RuntimeException("Can't find ingredient with id:" + ingredientId);
        }
        return ingredientToCommand.convert(optionalIngredient.get());
    }
}
