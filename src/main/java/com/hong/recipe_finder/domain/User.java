package com.hong.recipe_finder.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Setter
@Getter
@Entity
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "password")
    private String password;

    @Column(name = "provider")
    private String provider;

    @Column(name = "token")
    private String token;

    @Builder
    public User(String username, String email, String phone, String password, String provider, String token) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.provider = provider;
        this.token = token;
    }

    // 사용자의 이름이나 이메일을 업데이트하는 메소드
    public User updateUser(String username, String email) {
        this.username = username;
        this.email = email;

        return this;
    }
}
