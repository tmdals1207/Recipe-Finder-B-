package com.hong.recipe_finder.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CookingStepDTO {
    private String description;
    private int stepNumber;
    private String imageUrl;
}
