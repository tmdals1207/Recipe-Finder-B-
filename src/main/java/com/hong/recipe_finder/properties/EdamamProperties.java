package com.hong.recipe_finder.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "edamam")
public class EdamamProperties {

    private Recipe recipe;
    private Nutrition nutrition;
    private Meal meal;


    public static class Recipe {
        private String api;
        private String app;

    }

    public static class Nutrition {
        private String api;
        private String app;

    }

    public static class Meal {
        private String api;
        private String app;

    }
}
