package com.hong.recipe_finder.dto;

import com.hong.recipe_finder.domain.CookingStep;
import com.hong.recipe_finder.domain.Ingredient;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RecipeWithDetails {
    private String title;
    private String profileImage;
    private String authorProfile;
    private String summation;
    private String difficulty;
    private int cookingTime;
    private List<Ingredient> ingredients;
    private List<Ingredient> seasonings;
    private List<CookingStep> cookingSteps;
    private String tips;

    public RecipeWithDetails(String title, String profileImage, String authorProfile, String summation, String difficulty, int cookingTime, List<Ingredient> ingredients, List<Ingredient> seasonings, List<CookingStep> cookingSteps, String tips) {
        this.title = title;
        this.profileImage = profileImage;
        this.authorProfile = authorProfile;
        this.summation = summation;
        this.difficulty = difficulty;
        this.cookingTime = cookingTime;
        this.ingredients = ingredients;
        this.seasonings = seasonings;
        this.cookingSteps = cookingSteps;
        this.tips = tips;
    }

}
