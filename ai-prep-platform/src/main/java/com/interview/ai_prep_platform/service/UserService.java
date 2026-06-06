package com.interview.ai_prep_platform.service;

import com.interview.ai_prep_platform.entity.User;
import com.interview.ai_prep_platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) {
        // 1. Check if username already exists
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username is already taken!");
        }

        // 2. Check if email already exists
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already registered!");
        }

        // 3. Save the user to the database
        return userRepository.save(user);
    }

    public User loginUser(String email, String password) {
        // 1. Check if user exists by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email!"));

        // 2. Check if password matches
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password!");
        }

        // 3. Return the user if everything is correct
        return user;
    }
}