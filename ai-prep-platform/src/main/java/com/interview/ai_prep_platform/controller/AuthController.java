package com.interview.ai_prep_platform.controller;

import com.interview.ai_prep_platform.dto.AuthRequest;
import com.interview.ai_prep_platform.dto.AuthResponse;
import com.interview.ai_prep_platform.entity.User;
import com.interview.ai_prep_platform.repository.UserRepository;
import com.interview.ai_prep_platform.config.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    // 1. REGISTER ENDPOINT
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody AuthRequest request) {
        try {
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Error: Email is already in use!");
            }

            User user = new User();
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setUsername(request.getEmail());

            userRepository.save(user);

            String token = jwtUtils.generateToken(user.getEmail());
            return ResponseEntity.ok(new AuthResponse(token));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error during registration: " + e.getMessage());
        }
    }

    // 2. LOGIN ENDPOINT
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody AuthRequest request) {
        try {
            var userOptional = userRepository.findByEmail(request.getEmail());
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Error: Invalid email or password!");
            }

            User user = userOptional.get();

            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Error: Invalid email or password!");
            }

            String token = jwtUtils.generateToken(user.getEmail());
            return ResponseEntity.ok(new AuthResponse(token));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error during login: " + e.getMessage());
        }
    }
}