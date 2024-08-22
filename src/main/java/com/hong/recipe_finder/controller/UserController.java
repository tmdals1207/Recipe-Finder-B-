package com.hong.recipe_finder.controller;

import com.hong.recipe_finder.domain.User;
import com.hong.recipe_finder.dto.LoginResponse;
import com.hong.recipe_finder.repository.UserRepository;
import com.hong.recipe_finder.service.OAuth2Service;
import com.hong.recipe_finder.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hong.recipe_finder.dto.UserDto;
import com.hong.recipe_finder.security.JwtTokenProvider;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService userService;
    private final OAuth2Service oAuth2Service;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public UserController(UserService userService, OAuth2Service oAuth2Service, JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.userService = userService;
        this.oAuth2Service = oAuth2Service;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<String> signup(@RequestBody UserDto userDto) {
        log.info("회원가입 시작!");
        userService.register(userDto);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto) {
        log.info("로그인 시작!");
        User user = userService.login(userDto);

        if (user != null) {
            // 사용자 정보가 있을 경우
            String token = jwtTokenProvider.createToken(user.getEmail()); // 토큰 생성
            log.info("token: {}", token);
            user.setToken(token);
            userService.saveUser(user); // 업데이트된 사용자 정보를 저장
            return ResponseEntity.ok(new LoginResponse("Login successful", user, token)); // 토큰과 함께 응답
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        log.info("logout 시작!");
        String token = request.getHeader("Authorization").substring(7); // "Bearer " 제외

        // 토큰을 기반으로 사용자 조회
        String userEmail = jwtTokenProvider.parseToken(token).getSubject();
        User user = userRepository.findByEmail(userEmail);

        if (user != null) {
            if (user.getProvider() != null && !user.getProvider().isEmpty()) {
                // OAuth 로그아웃 처리
                user.setToken(null);
                userService.saveUser(user);
            } else {
                // 일반 로그아웃 처리
                userService.logout(user);
            }
        }

        return ResponseEntity.ok("Logout successful");
    }
}
