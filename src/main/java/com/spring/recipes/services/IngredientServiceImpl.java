package com.spring.recipes.services;

import com.spring.recipes.command.IngredientCommand;
import com.spring.recipes.command.RecipeCommand;
import com.spring.recipes.converters.IngredientCommandToIngredient;
import com.spring.recipes.converters.IngredientToIngredientCommand;
import com.spring.recipes.domain.Ingredient;
import com.spring.recipes.domain.Recipe;
import com.spring.recipes.domain.UnitOfMeasure;
import com.spring.recipes.repositories.IngredientRepository;
import com.spring.recipes.repositories.RecipeRepository;
import com.spring.recipes.repositories.UnitOfMeasureRepository;
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
    private UnitOfMeasureRepository uomRepository;
    private RecipeService recipeService;
    private RecipeRepository recipeRepository;
    private IngredientToIngredientCommand ingredientToCommand;
    private IngredientCommandToIngredient commandToIngredient;

    public IngredientServiceImpl(IngredientRepository ingredientRepository,
                                 UnitOfMeasureRepository uomRepository,
                                 RecipeService recipeService,
                                 RecipeRepository recipeRepository,
                                 IngredientToIngredientCommand ingredientToCommand,
                                 IngredientCommandToIngredient commandToIngredient) {
        this.ingredientRepository = ingredientRepository;
        this.uomRepository = uomRepository;
        this.recipeService = recipeService;
        this.recipeRepository = recipeRepository;
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
        Recipe recipe = recipeService.findById(ingredientCommand.getRecipeId());

        Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId()))
                .findFirst();
        if (ingredientOptional.isPresent()) {
            Ingredient ingredient = ingredientOptional.get();
            ingredient.setDescription(ingredientCommand.getDescription());
            ingredient.setAmount(ingredientCommand.getAmount());
            UnitOfMeasure unitOfMeasure = uomRepository.findById(ingredientCommand.getUnitOfMeasure().getId())
                    .orElseThrow(() -> new RuntimeException("Unit of Measure not found!"));
            ingredient.setUnitOfMeasure(unitOfMeasure);
        } else {
            Ingredient ingredientToSave = commandToIngredient.convert(ingredientCommand);
            if (ingredientToSave != null) {
                recipe.addIngredient(ingredientToSave);
                ingredientToSave.setRecipe(recipe);
            } else {
                throw new RuntimeException("No ingredient to save!");
            }
        }
        Recipe savedRecipe = recipeRepository.save(recipe);

        Optional<Ingredient> savedIngredient = savedRecipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId()))
                .findFirst();
        if (!savedIngredient.isPresent()) {
            savedIngredient = savedRecipe.getIngredients().stream()
                    .filter(recipeIngredient ->
                            recipeIngredient.getDescription()
                                    .equals(ingredientCommand.getDescription()))
                    .filter(recipeIngredient ->
                            recipeIngredient.getAmount()
                                    .equals(ingredientCommand.getAmount()))
                    .filter(recipeIngredient ->
                            recipeIngredient.getUnitOfMeasure().getId()
                                    .equals(ingredientCommand.getUnitOfMeasure().getId()))
                    .findFirst();
        }
        return ingredientToCommand.convert(savedIngredient.get());
    }

    @Override
    public void deleteById(Long id) {
        ingredientRepository.deleteById(id);
        log.debug("Ingredient with id:" + id + " deleted!");
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
