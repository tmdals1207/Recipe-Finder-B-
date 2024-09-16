package com.hong.recipe_finder.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.token-validity-in-milliseconds}")
    private long tokenValidityInMilliseconds;

    private Key key;

    // 키 초기화
    @PostConstruct
    protected void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes); //  HMAC SHA key 생성
    }

    // 토큰 생성
    public String createToken(String userEmail) {
        Claims claims = Jwts.claims().setSubject(userEmail);

        Date now = new Date();
        Date validity = new Date(now.getTime() + tokenValidityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256) // deprecated 메서드 수정
                .compact();
    }

    // 토큰 파싱
    public Claims parseToken(String token) {
        return Jwts.parserBuilder() // deprecated 메서드 수정
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder() // deprecated 메서드 수정
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // JWT 토큰으로부터 인증 정보 조회
    public Authentication getAuthentication(String token) {
        Claims claims = parseToken(token);
        String username = claims.getSubject();
        List<SimpleGrantedAuthority> authorities = ((List<?>) claims.get("roles")).stream()
                .map(role -> new SimpleGrantedAuthority((String) role))
                .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(username, "", authorities);
    }
}
