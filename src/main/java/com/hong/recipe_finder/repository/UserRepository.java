package com.hong.recipe_finder.repository;

import com.hong.recipe_finder.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmailAndProvider(String email, String provider);

    User findByEmail(String email);

    User findByToken(String actualToken);
}
