package com.hong.recipe_finder.controller;

import com.hong.recipe_finder.domain.User;
import com.hong.recipe_finder.dto.LoginResponse;
import com.hong.recipe_finder.repository.UserRepository;
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
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "Authorization")

public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<String> signup(@RequestBody UserDto userDto) {
        log.info("회원가입 시작!");
        userService.register(userDto);
        return ResponseEntity.ok("User registered successfully");
    }

    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsername(@RequestParam String username) {
        boolean isAvailable = userService.checkUsernameAvailability(username);
        return ResponseEntity.ok(isAvailable);
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
        log.info("request: {}", request.getHeader("Authorization"));
        log.info("받아온 토큰 : {}", token);
        if (token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        User user = userRepository.findByToken(token); // 토큰으로 사용자 조회

        if (user != null) {
            if (user.getProvider() != null && !user.getProvider().isEmpty()) {
                // OAuth 로그아웃 처리
                user.setToken(null);
                userService.saveUser(user);
                log.info("OAuth User {} logged out", user.getEmail());

            } else {
                // 일반 로그인 사용자의 로그아웃 처리
                userService.logout(user);
                log.info("User {} logged out", user.getEmail());
            }
            return ResponseEntity.ok("Logout successful");
        } else {
            log.error("User not found with token: {}", token);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }
    }

    @PutMapping("/change_username")
    public ResponseEntity<String> changeUsername(@RequestBody UserDto userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isUpdated = userService.changeUsername(userDto.getId(), userDto.getUsername());

        if (isUpdated) {
            return ResponseEntity.ok("Username updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }


    @PutMapping("/change_email")
    public ResponseEntity<String> changeEmail(@RequestBody UserDto userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isUpdated = userService.changeEmail(userDto.getId(), userDto.getEmail());

        if (isUpdated) {
            return ResponseEntity.ok("Email updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @PutMapping("/change_phone")
    public ResponseEntity<String> changePhone(@RequestBody UserDto userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isUpdated = userService.changePhone(userDto.getId(), userDto.getPhone());

        if (isUpdated) {
            return ResponseEntity.ok("Phone number updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @PutMapping("/change_password")
    public ResponseEntity<String> changePassword(@RequestBody UserDto userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isUpdated = userService.changePassword(userDto.getId(), userDto.getPassword());

        if (isUpdated) {
            return ResponseEntity.ok("Password updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

}
