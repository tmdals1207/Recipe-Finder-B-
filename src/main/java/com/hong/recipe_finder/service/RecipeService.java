package com.hong.recipe_finder.service;

import com.hong.recipe_finder.domain.Recipe;
import com.hong.recipe_finder.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository; // Repository 계층 사용

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();

    }

    public Recipe createRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public Optional<Recipe> getRecipeById(Long id) {
        return recipeRepository.findById(id);
    }

    public Optional<Recipe> updateRecipe(Long id, Recipe recipeDetails) {
        return recipeRepository.findById(id)
                .map(recipe -> {
                    recipe.setName(recipeDetails.getName());
                    recipe.setIngredients(recipeDetails.getIngredients());
                    recipe.setInstructions(recipeDetails.getInstructions());
                    return recipeRepository.save(recipe);
                });
    }

    public Optional<Recipe> deleteRecipe(Long id) {
        return recipeRepository.findById(id)
                .map(recipe -> {
                    recipeRepository.delete(recipe);
                    return recipe;
                });
    }
}
