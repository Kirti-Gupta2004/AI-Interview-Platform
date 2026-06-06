package com.interview.ai_prep_platform.repository;

import com.interview.ai_prep_platform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Custom methods to check duplicates during registration
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}