package com.hong.recipe_finder.controller;

import com.hong.recipe_finder.service.SpoonacularService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:3000") // 프론트엔드 포트
public class SpoonacularController {

    @Autowired
    private SpoonacularService spoonacularService;

    @GetMapping("/api/spoonacular/search")
    public String searchRecipes(@RequestParam String query) {
        log.info("spoonacular 검색 실행");
        return spoonacularService.searchRecipes(query);
    }
}