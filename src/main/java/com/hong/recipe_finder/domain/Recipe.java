package com.hong.recipe_finder.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 고유 번호

    private String profileImage; // 프로필 사진
    private String summation; // 요약
    private int views; // 조회수
    private String authorProfile; // 작성자 프로필
    private String title; // 제목
    private String content; // 내용
    private int servings; // 몇인분
    private int cookingTime; // 걸리는 시간 (분 단위)
    private String difficulty; // 난이도 (e.g. 쉬움, 중간, 어려움)

    @ElementCollection
    @CollectionTable(name = "recipe_ingredients", joinColumns = @JoinColumn(name = "recipe_id"))
    private List<Ingredient> ingredients = new ArrayList<>(); // 재료

    @ElementCollection
    @CollectionTable(name = "recipe_seasonings", joinColumns = @JoinColumn(name = "recipe_id"))
    private List<Ingredient> seasonings = new ArrayList<>(); // 양념

    @Lob
    private String knowHow; // 노하우

    private String videoUrl; // 동영상 URL

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<CookingStep> cookingSteps = new ArrayList<>(); // 조리 순서

    @Lob
    private String tips; // 팁과 주의사항

    private LocalDateTime createdDate; // 등록일
    private LocalDateTime updatedDate; // 수정일

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>(); // 리뷰

    @ElementCollection
    @CollectionTable(name = "recipe_tags", joinColumns = @JoinColumn(name = "recipe_id"))
    private List<String> tags = new ArrayList<>(); // 태그

    private double rating; // 별점

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>(); // 댓글

    // 영양정보 합산 필드
    private double totalCalories;
    private double totalFat;
    private double totalSaturatedFat;
    private double totalTransFat;
    private double totalMonounsaturatedFat;
    private double totalPolyunsaturatedFat;
    private double totalCarbs;
    private double totalFiber;
    private double totalSugars;
    private double totalProtein;
    private double totalCholesterol;
    private double totalSodium;
    private double totalCalcium;
    private double totalMagnesium;
    private double totalPotassium;
    private double totalIron;
    private double totalPhosphorus;
    private double totalVitaminA;
    private double totalVitaminC;
    private double totalThiaminB1;
    private double totalRiboflavinB2;
    private double totalNiacinB3;
    private double totalVitaminB6;
    private double totalFolateEquivalent;
    private double totalVitaminB12;
    private double totalVitaminD;
    private double totalVitaminE;
    private double totalVitaminK;


}