package com.hong.recipe_finder.service;

import com.hong.recipe_finder.dto.UserDto;
import com.hong.recipe_finder.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.hong.recipe_finder.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void signup(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);
    }

    public boolean login(UserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail());
        if (user != null && passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            return true;
        }
        return false;
    }
}

