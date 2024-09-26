package com.hong.recipe_finder.service;

import com.hong.recipe_finder.dto.UserDto;
import com.hong.recipe_finder.domain.User;
import com.hong.recipe_finder.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

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

    public boolean changeUsername(Long id, String newUsername) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // 새로운 닉네임이 다른 유저에게 사용 중인 경우
            if (userRepository.existsByUsernameAndIdNot(newUsername, id)) {
                throw new IllegalArgumentException("해당 닉네임은 이미 다른 사용자가 사용 중입니다.");
            }

            user.setUsername(newUsername);
            userRepository.save(user);  // 유저 이름 변경 후 저장
            return true;
        } else {
            return false;  // 유저를 찾지 못한 경우
        }
    }

    public boolean changeEmail(Long id, String newEmail) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // 새로운 이메일이 다른 유저에게 사용 중인 경우
            if (userRepository.existsByEmailAndIdNot(newEmail, id)) {
                throw new IllegalArgumentException("해당 이메일은 이미 다른 사용자가 사용 중입니다.");
            }

            user.setEmail(newEmail);
            userRepository.save(user);  // 이메일 변경 후 저장
            return true;
        } else {
            return false;  // 유저를 찾지 못한 경우
        }
    }

    public boolean changePhone(Long id, String newPhone) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // 새로운 전화번호가 다른 유저에게 사용 중인 경우
            if (userRepository.existsByPhoneAndIdNot(newPhone, id)) {
                throw new IllegalArgumentException("해당 전화번호는 이미 다른 사용자가 사용 중입니다.");
            }

            user.setPhone(newPhone);
            userRepository.save(user);  // 전화번호 변경 후 저장
            return true;
        } else {
            return false;  // 유저를 찾지 못한 경우
        }
    }

    public boolean changePassword(Long id, String newPassword) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);  // 비밀번호 변경 후 저장
            return true;
        } else {
            return false;  // 유저를 찾지 못한 경우
        }
    }


}
