package com.hong.recipe_finder.dto;

import com.hong.recipe_finder.domain.User;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDto {
    private String username; // 사용자 이름

    private String provider; // 로그인한 서비스

    private String email; // 사용자의 이메일

    private String phone;

    private String password; //

    private String token;

    public void setUserName(String userName) {
        this.username = userName;
    }

    // DTO 파일을 통하여 Entity를 생성하는 메소드
    public User toEntity() {
        return User.builder()
                .username(this.username)
                .email(this.email)
                .phone(this.phone)
                .provider(this.provider)
                .password(this.password)
                .token(this.token)
                .build();
    }
}