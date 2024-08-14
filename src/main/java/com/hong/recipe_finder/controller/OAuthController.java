package com.hong.recipe_finder.controller;

import com.hong.recipe_finder.service.OAuth2Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/oauth2")
@CrossOrigin(origins = "http://localhost:3000") // 프론트엔드 포트
public class OAuthController {


    private OAuth2Service oAuth2Service;

//    @GetMapping("/authorization/{provider}")
//    public ResponseEntity<String> login(@PathVariable String provider) {
//        boolean isAuthenticated = oAuth2Service.authenticate(provider);
//        if (isAuthenticated) {
//            return ResponseEntity.ok("OAuth Login successful");
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("OAuth Login failed");
//        }
//    }

    @GetMapping("/loginInfo")
    public ResponseEntity<Map<String, Object>> getJson(Authentication authentication) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        return ResponseEntity.ok(attributes);
    }

}
