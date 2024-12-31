package com.hong.recipe_finder.security;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        // HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 요청 헤더에서 JWT 토큰을 가져옴
        String token = resolveToken(httpRequest);
        log.info("JWT Token: {}", token);

        // 현재 컨텍스트에 인증 정보가 없고 JWT 토큰이 유효할 경우 JWT 인증 처리
        if (token != null && jwtTokenProvider.validateToken(token)) {
            Claims claims = jwtTokenProvider.parseToken(token);
            String userEmail = claims.getSubject();
            String username = claims.get("username", String.class);
            log.info("Authenticated user: {}, Username: {}", userEmail, username);
            CustomUserDetails userDetails = new CustomUserDetails(userEmail, username);

            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))));

        }
        // 만약 인증 정보가 이미 존재하고 OAuth2 인증인 경우 JWT 인증을 건너뜀
        else if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            log.info("OAuth2 Authentication detected, bypassing JWT validation.");
            log.info("Authenticated with OAuth2: {}", SecurityContextHolder.getContext().getAuthentication());
        }
        // JWT 토큰이 없거나 유효하지 않은 경우
        else {
            log.info("Token is null or invalid");
        }

        // 필터 체인 진행
        chain.doFilter(request, response);
    }

    // 요청 헤더에서 JWT 토큰을 추출
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}