package com.hong.recipe_finder.controller;

import com.hong.recipe_finder.domain.Recipe;
import com.hong.recipe_finder.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:3000") // 프론트엔드 포트
@RequestMapping("/api/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService; // Service 계층 사용

    @GetMapping("/all")
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        log.info("레시피 모두 가져오기 요청 수신됨");  // 요청 수신 시 로그 출력
        List<Recipe> recipes = recipeService.getAllRecipes();
        if (recipes.isEmpty()) {
            log.warn("레시피 목록이 비어 있습니다.");
        }
        return ResponseEntity.ok(recipes);
    }

    @PostMapping("/create")
    public ResponseEntity<Recipe> createRecipe(@RequestBody Recipe recipe) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        recipe.setAuthorProfile(currentUserName);  // 작성자 프로필 설정
        Recipe createdRecipe = recipeService.createRecipe(recipe);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRecipe);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable Long id) {
        return recipeService.getRecipeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteRecipe(@PathVariable Long id) {
        return recipeService.deleteRecipe(id)
                .map(recipe -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }
}
