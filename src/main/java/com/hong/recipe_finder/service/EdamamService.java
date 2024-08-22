package com.hong.recipe_finder.service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class EdamamService {

    @Value("${edamam.recipe.api.key}")
    private String recipeApiKey;

    @Value("${edamam.recipe.app.id}")
    private String recipeAppId;

    @Value("${edamam.nutrition.api.key}")
    private String nutritionApiKey;

    @Value("${edamam.nutrition.app.id}")
    private String nutritionAppId;

    @Value("${edamam.meal.api.key}")
    private String mealApiKey;

    @Value("${edamam.meal.app.id}")
    private String mealAppId;

    private final OkHttpClient client = new OkHttpClient();

    // 레시피 검색
    public Map<String, Object> searchRecipes(String query) {
        String url = "https://api.edamam.com/search?q={query}&app_id={recipeAppId}&app_key={recipeApiKey}&from=0&to=5";

        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> params = new HashMap<>();
        params.put("query", query);
        params.put("recipeAppId", recipeAppId);
        params.put("recipeApiKey", recipeApiKey);

        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {},
                params
        );

        return responseEntity.getBody();
    }

    // 영양 정보 검색
    public String getNutritionInfo(String recipeUrl) {
        String url = "https://api.edamam.com/api/nutrition-details?app_id=" + nutritionAppId + "&app_key=" + nutritionApiKey + "&url=" + recipeUrl;
        return executeRequest(url);
    }

    // 식단 추천
    public String getMealRecommendations(String dietType) {
        String url = "https://api.edamam.com/api/mealplanner/v2/" + dietType + "?app_id=" + mealAppId + "&app_key=" + mealApiKey;
        return executeRequest(url);
    }

    // HTTP 요청을 실행하고 결과를 반환하는 메서드
    private String executeRequest(String url) {
        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            if (response.body() == null) {
                throw new IOException("Empty response body");
            }
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}
