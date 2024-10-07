package com.hong.recipe_finder.controller;

import com.hong.recipe_finder.service.EdamamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:3000") // 프론트엔드 포트
public class EdamamController {

    private final EdamamService edamamService;

    public EdamamController(EdamamService edamamService) {
        this.edamamService = edamamService;
    }

    // 레시피 검색
    @GetMapping("/api/edamam/search")
    @SuppressWarnings("unchecked")  // 경고 억제
    public List<Map<String, String>> searchRecipes(@RequestParam String query) {
        log.info("Searching recipes from Edamam with query: {}", query);
        Map<String, Object> response = edamamService.searchRecipes(query);

        List<Map<String, String>> recipes = new ArrayList<>();
        if (response.containsKey("hits")) {
            List<Map<String, Object>> hits = (List<Map<String, Object>>) response.get("hits");
            for (Map<String, Object> hit : hits) {
                Map<String, Object> recipe = (Map<String, Object>) hit.get("recipe");
                Map<String, String> recipeInfo = new HashMap<>();
                recipeInfo.put("title", (String) recipe.get("label"));
                recipeInfo.put("image", (String) recipe.get("image"));
                recipeInfo.put("url", (String) recipe.get("url"));
                recipes.add(recipeInfo);
            }
        }
        return recipes;
    }

    // 영양 정보 검색
    @GetMapping("/api/edamam/nutrition")
    public String getNutritionInfo(@RequestParam String recipeUrl) {
        log.info("Edamam 영양 정보 검색 실행");
        log.info((recipeUrl));
        return edamamService.getNutritionInfo(recipeUrl);
    }

    // 식단 추천
    @GetMapping("/api/edamam/meal-planner")
    public String getMealRecommendations(@RequestParam String dietType) {
        log.info("Edamam 식단 추천 실행");
        return edamamService.getMealRecommendations(dietType);
    }
}
