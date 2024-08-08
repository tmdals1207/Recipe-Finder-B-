package com.hong.recipe_finder.repository;

import com.hong.recipe_finder.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
