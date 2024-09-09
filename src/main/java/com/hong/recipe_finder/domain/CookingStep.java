package com.hong.recipe_finder.domain;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;


@Getter
@Setter
@Entity
public class CookingStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe; // 조리 과정이 속한 레시피

    @Lob
    private String description; // 조리 설명

    private String imageUrl; // 조리 사진 URL

    private int stepNumber; // 조리 순서 번호

    // Constructors, Getters and Setters, etc.

    public CookingStep() {}

    public CookingStep(Recipe recipe, String description, String imageUrl, int stepNumber) {
        this.recipe = recipe;
        this.description = description;
        this.imageUrl = imageUrl;
        this.stepNumber = stepNumber;
    }
}
