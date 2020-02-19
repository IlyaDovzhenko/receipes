package com.spring.recipes.services;

import com.spring.recipes.command.RecipeCommand;
import com.spring.recipes.converters.RecipeCommandToRecipe;
import com.spring.recipes.converters.RecipeToRecipeCommand;
import com.spring.recipes.domain.Recipe;
import com.spring.recipes.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;

    public RecipeServiceImpl(RecipeRepository recipeRepository,
                             RecipeCommandToRecipe recipeCommandToRecipe,
                             RecipeToRecipeCommand recipeToRecipeCommand) {
        this.recipeRepository = recipeRepository;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
    }

    @Override
    public Set<Recipe> getRecipes() {
        Set<Recipe> recipes = new HashSet<>();
        recipeRepository.findAll().iterator().forEachRemaining(recipes::add);
        return recipes;
    }

    @Override
    public Recipe findById(Long id) {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
        if (!optionalRecipe.isPresent())
            throw new RuntimeException("Recipe with id:" + id + " Not Found!");
        return optionalRecipe.orElse(null);
    }

    @Override
    @Transactional
    public RecipeCommand findCommandById(Long id) {
        return recipeToRecipeCommand.convert(findById(id));
    }

    @Override
    @Transactional
    public RecipeCommand saveRecipeCommand(RecipeCommand recipeCommand) {
        Recipe recipeToSave = recipeCommandToRecipe.convert(recipeCommand);

        if (recipeToSave != null) {
            recipeRepository.save(recipeToSave);
            log.debug("Saved recipe id: " + recipeToSave.getId());
            return recipeToRecipeCommand.convert(recipeToSave);
        } else {
            throw new RuntimeException("Problem with convert recipe!");
        }
    }

    @Override
    public void deleteById(Long id) {
        recipeRepository.deleteById(id);
    }
}
