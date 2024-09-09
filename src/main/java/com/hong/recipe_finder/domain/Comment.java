package com.hong.recipe_finder.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe; // 댓글이 속한 레시피

    private String authorProfile; // 작성자 프로필
    private String content; // 댓글 내용
    private LocalDateTime createdDate; // 댓글 작성일

    // Constructors, Getters and Setters, etc.

    public Comment() {}

    public Comment(Recipe recipe, String authorProfile, String content, LocalDateTime createdDate) {
        this.recipe = recipe;
        this.authorProfile = authorProfile;
        this.content = content;
        this.createdDate = createdDate;
    }

    // Getters and Setters
}
