package com.hong.recipe_finder.service;

import com.hong.recipe_finder.dto.UserDto;
import com.hong.recipe_finder.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.hong.recipe_finder.repository.UserRepository;


@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        log.info("user 저장 완료");
        userRepository.save(user);
    }

    public boolean login(UserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail());
        return user != null && passwordEncoder.matches(userDto.getPassword(), user.getPassword());
    }
}

