package com.jay.javaspringmsuser.repository;

import com.jay.javaspringmsuser.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    User save(User user);
    Optional<User> findByEmail(String email);
}
