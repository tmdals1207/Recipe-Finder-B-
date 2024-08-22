package com.hong.recipe_finder.dto;

import com.hong.recipe_finder.domain.User;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginResponse {
    // getters and setters
    private String message;
    private User user;
    private String token;

    public LoginResponse(String message, User user, String token) {
        this.message = message;
        this.user = user;
        this.token = token;
    }
}