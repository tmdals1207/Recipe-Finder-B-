package com.hong.recipe_finder.controller;

import com.hong.recipe_finder.service.OAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/oauth")
public class OAuthController {

    @Autowired
    private OAuthService oAuthService;

    @GetMapping("/login/{provider}")
    public ResponseEntity<String> login(@PathVariable String provider, @RequestParam String token) {
        boolean isAuthenticated = oAuthService.authenticate(provider, token);
        if (isAuthenticated) {
            return ResponseEntity.ok("OAuth Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("OAuth Login failed");
        }
    }
}
