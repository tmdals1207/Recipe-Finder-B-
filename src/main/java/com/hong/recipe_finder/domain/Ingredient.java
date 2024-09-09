package com.hong.recipe_finder.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Embeddable
public class Ingredient {
    private String name; // 재료명
    private String quantity; // 양 (예: 1컵, 100g)

    // 영양정보
    private double calories; // 칼로리
    private double fat; // 지방 (g)
    private double saturatedFat; // 포화지방 (g)
    private double transFat; // 트랜스지방 (g)
    private double monounsaturatedFat; // 단일불포화지방 (g)
    private double polyunsaturatedFat; // 다중불포화지방 (g)
    private double carbs; // 탄수화물 (g)
    private double fiber; // 식이섬유 (g)
    private double sugars; // 당 (g)
    private double protein; // 단백질 (g)
    private double cholesterol; // 콜레스테롤 (mg)
    private double sodium; // 나트륨 (mg)
    private double calcium; // 칼슘 (mg)
    private double magnesium; // 마그네슘 (mg)
    private double potassium; // 칼륨 (mg)
    private double iron; // 철분 (mg)
    private double phosphorus; // 인 (mg)
    private double vitaminA; // 비타민 A (µg)
    private double vitaminC; // 비타민 C (mg)
    private double thiaminB1; // 티아민 (B1) (mg)
    private double riboflavinB2; // 리보플라빈 (B2) (mg)
    private double niacinB3; // 나이아신 (B3) (mg)
    private double vitaminB6; // 비타민 B6 (mg)
    private double folateEquivalent; // 엽산 (µg DFE)
    private double vitaminB12; // 비타민 B12 (µg)
    private double vitaminD; // 비타민 D (µg)
    private double vitaminE; // 비타민 E (mg)
    private double vitaminK; // 비타민 K (µg)

    // Constructors, Getters and Setters, etc.

    public Ingredient() {}

    public Ingredient(String name, String quantity, double calories, double fat, double saturatedFat, double transFat,
                      double monounsaturatedFat, double polyunsaturatedFat, double carbs, double fiber, double sugars,
                      double protein, double cholesterol, double sodium, double calcium, double magnesium, double potassium,
                      double iron, double phosphorus, double vitaminA, double vitaminC, double thiaminB1, double riboflavinB2,
                      double niacinB3, double vitaminB6, double folateEquivalent, double vitaminB12, double vitaminD,
                      double vitaminE, double vitaminK) {
        this.name = name;
        this.quantity = quantity;
        this.calories = calories;
        this.fat = fat;
        this.saturatedFat = saturatedFat;
        this.transFat = transFat;
        this.monounsaturatedFat = monounsaturatedFat;
        this.polyunsaturatedFat = polyunsaturatedFat;
        this.carbs = carbs;
        this.fiber = fiber;
        this.sugars = sugars;
        this.protein = protein;
        this.cholesterol = cholesterol;
        this.sodium = sodium;
        this.calcium = calcium;
        this.magnesium = magnesium;
        this.potassium = potassium;
        this.iron = iron;
        this.phosphorus = phosphorus;
        this.vitaminA = vitaminA;
        this.vitaminC = vitaminC;
        this.thiaminB1 = thiaminB1;
        this.riboflavinB2 = riboflavinB2;
        this.niacinB3 = niacinB3;
        this.vitaminB6 = vitaminB6;
        this.folateEquivalent = folateEquivalent;
        this.vitaminB12 = vitaminB12;
        this.vitaminD = vitaminD;
        this.vitaminE = vitaminE;
        this.vitaminK = vitaminK;
    }
}