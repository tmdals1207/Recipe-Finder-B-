package com.hong.recipe_finder.config;

import io.micrometer.common.lang.NonNullApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@NonNullApi
public class WebConfig implements WebMvcConfigurer {


    @Bean
    public CorsConfiguration corsConfiguration() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(Collections.singletonList("http://localhost:3000")); // 허용할 오리진
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE")); // 허용할 메소드
        corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type")); // 허용할 헤더
        corsConfig.setAllowCredentials(true); // 인증 정보 허용
        return corsConfig;
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") // 프론트엔드의 URL 을 추가
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 허용할 HTTP 메소드
                .allowedHeaders("*") // 허용할 HTTP 헤더
                .allowCredentials(true); // 인증 정보를 포함한 요청을 허용
    }
}