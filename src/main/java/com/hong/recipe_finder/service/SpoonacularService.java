package com.hong.recipe_finder.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SpoonacularService {

    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${spoonacular.api.key}")
    private String apiKey;

    public String searchRecipes(String query) {
        String url = "https://api.spoonacular.com/recipes/complexSearch?query=" + query + "&apiKey=" + apiKey;
        return restTemplate.getForObject(url, String.class);
    }
}