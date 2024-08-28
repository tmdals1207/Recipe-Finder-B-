package com.hong.recipe_finder.controller;

import com.hong.recipe_finder.domain.User;
import com.hong.recipe_finder.repository.UserRepository;
import com.hong.recipe_finder.service.OAuth2Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth2")
@CrossOrigin(origins = "http://localhost:3000") // 프론트엔드 포트
public class OAuthController {


    private final OAuth2Service oAuth2Service;
    private final UserRepository userRepository;

    @GetMapping("/loginInfo")
    public ResponseEntity<Map<String, Object>> getJson() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof OAuth2User oAuth2User)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Unauthorized"));
        }
        Map<String, Object> attributes = oAuth2User.getAttributes();

        return ResponseEntity.ok(attributes);
    }



    @GetMapping("/callback")
    public ResponseEntity<Map<String, Object>> oauth2Callback(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = oAuth2Service.loadUser(userRequest);

        // JWT 토큰 및 사용자 정보를 포함하는 응답 생성
        Map<String, Object> response = new HashMap<>();
        response.put("token", oAuth2User.getAttributes().get("token")); // JWT 토큰
        response.put("user", oAuth2User.getAttributes()); // 사용자 정보

        return ResponseEntity.ok(response);
    }

    @GetMapping("/getUser")
    public ResponseEntity<User> getUser(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        // Authorization 헤더에서 "Bearer " 문자열을 제거하고 실제 토큰 값만 추출
        String actualToken = token.replace("Bearer ", "");

        // 이메일을 통해 사용자 검색
        User user = userRepository.findByToken(actualToken);
        return ResponseEntity.ok(user);
    }

}
