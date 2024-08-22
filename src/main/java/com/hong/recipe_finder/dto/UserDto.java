package com.hong.recipe_finder.dto;

import com.hong.recipe_finder.domain.User;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDto {
    private String username; // 사용자 이름
    @Setter
    private String provider; // 로그인한 서비스
    @Setter
    private String email; // 사용자의 이메일
    @Setter
    private String password; //
    @Setter
    private String token;

    public void setUserName(String userName) {
        this.username = userName;
    }

    // DTO 파일을 통하여 Entity를 생성하는 메소드
    public User toEntity() {
        return User.builder()
                .username(this.username)
                .email(this.email)
                .provider(this.provider)
                .password(this.password)
                .token(this.token)
                .build();
    }
}