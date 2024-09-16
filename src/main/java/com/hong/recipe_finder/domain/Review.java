package com.hong.recipe_finder.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe; // 리뷰가 속한 레시피

    @Lob
    private String content; // 리뷰 내용

    private String imageUrl; // 리뷰 사진 URL (있으면 포토리뷰)

    private boolean isPhotoReview; // 포토 리뷰 여부

    private double rating; // 리뷰 별점

    public Review() {
    }
}
