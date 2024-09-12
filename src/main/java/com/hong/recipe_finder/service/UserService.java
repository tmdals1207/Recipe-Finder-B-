package com.hong.recipe_finder.service;

import com.hong.recipe_finder.dto.UserDto;
import com.hong.recipe_finder.domain.User;
import com.hong.recipe_finder.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(UserDto userDto) {
        if (userRepository.findByUsername(userDto.getUsername()) != null) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        user.setPhone(userDto.getPhone());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        log.info("user 저장 완료");
        userRepository.save(user);
    }

    public boolean checkUsernameAvailability(String username) {
        return userRepository.findByUsername(username) == null;
    }

    public User login(UserDto userDto) {
        log.info("유저의 이메일 : {}", userDto.getEmail());
        User user = userRepository.findByEmail(userDto.getEmail());
        if (user != null && passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            return user; // 사용자 객체 반환
        }
        return null; // 인증 실패 시 null 반환
    }

    public void saveUser(User user) {
        userRepository.save(user); // 사용자를 저장
    }

    public void logout(User user) {
        user.setToken(null); // 토큰 무효화
        saveUser(user); // 업데이트된 사용자 정보를 저장
        log.info("User {} logged out", user.getEmail());
    }
}
