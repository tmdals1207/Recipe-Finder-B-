package com.hong.recipe_finder.repository;

import com.hong.recipe_finder.domain.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    // 레시피 아이디로 재료 목록을 가져오는 메서드
    List<Ingredient> findByRecipeId(Long recipeId);
}