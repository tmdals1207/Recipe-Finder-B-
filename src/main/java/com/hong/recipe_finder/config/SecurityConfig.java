package com.hong.recipe_finder.config;

import com.hong.recipe_finder.service.OAuth2Service;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final OAuth2Service oAuth2Service;

    public SecurityConfig(OAuth2Service oAuth2Service) {
        this.oAuth2Service = oAuth2Service;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // CSRF 보안 설정 사용 X
                .logout(AbstractHttpConfigurer::disable) // 로그아웃 사용 X
                .formLogin(AbstractHttpConfigurer::disable) // 폼 로그인 사용 X
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").permitAll() // /api/** 경로를 인증 없이 허용
                        .anyRequest().permitAll() // 그 외의 모든 요청도 일단은 허용
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler((request, response, authentication) -> {
                            response.sendRedirect("http://localhost:3000"); // 로그인 성공 시 리디렉션할 URL
                        })
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2Service) // OAuth2 사용자 서비스 설정
                        )
                );

        return http.build(); // SecurityFilterChain 빌드
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)  // CSRF 비활성화
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/**").permitAll() // /api/** 경로를 인증 없이 허용
//                        .anyRequest().permitAll() // 그 외의 모든 요청도 일단은 허용
//                )
//                .formLogin(AbstractHttpConfigurer::disable) // 폼 로그인 비활성화
//                .httpBasic(AbstractHttpConfigurer::disable); // HTTP 기본 인증 비활성화
//
//        return http.build();
//    }