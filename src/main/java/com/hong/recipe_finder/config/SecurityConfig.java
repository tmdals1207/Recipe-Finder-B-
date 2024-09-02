package com.hong.recipe_finder.config;

import com.hong.recipe_finder.domain.User;
import com.hong.recipe_finder.repository.UserRepository;
import com.hong.recipe_finder.security.JwtFilter;
import com.hong.recipe_finder.security.JwtTokenProvider;
import com.hong.recipe_finder.service.OAuth2Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.util.Optional;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final OAuth2Service oAuth2Service;
    private final JwtTokenProvider jwtTokenProvider;
    private  final UserRepository userRepository;

    public SecurityConfig(OAuth2Service oAuth2Service, JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.oAuth2Service = oAuth2Service;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // CSRF 보안 설정 사용 X
                .logout(AbstractHttpConfigurer::disable) // 로그아웃 사용 X
                .formLogin(AbstractHttpConfigurer::disable) // 폼 로그인 사용 X
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").permitAll() // /api/** 경로를 인증 없이 허용
                        .anyRequest().permitAll() // 그 외의 모든 요청은 일단 허용
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler((request, response, authentication) -> {
                            String token = getToken(authentication);
                            log.info(token);

                            // 프론트엔드로 리디렉션할 URL
                            String redirectUrl = "http://localhost:3000/oauth2/callback?token=" + token;
                            // 리디렉션
                            response.sendRedirect(redirectUrl);
                        })
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2Service) // OAuth2 사용자 서비스 설정
                        )
                )
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class); // JWT 필터 추가

        return http.build(); // SecurityFilterChain 빌드
    }

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter(jwtTokenProvider); // JwtTokenProvider를 사용하는 JwtFilter 빈 생성
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() { // security를 적용하지 않을 리소스
        return web -> web.ignoring()
                .requestMatchers("/error", "/favicon.ico");
    }

    public String getToken(Authentication authentication) {
        String email = ((DefaultOAuth2User) authentication.getPrincipal()).getAttribute("email");
        String provider = ((DefaultOAuth2User) authentication.getPrincipal()).getAttribute("provider");

        User user = userRepository.findUserByEmailAndProvider(email, provider);
        return user.getToken();
    }
}
