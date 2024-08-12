package com.hong.recipe_finder.repository;

import com.hong.recipe_finder.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByProviderAndProviderId(String provider, String providerId);

    User findByEmail(String email);
}
