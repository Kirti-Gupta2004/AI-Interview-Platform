package com.interview.ai_prep_platform.config;

import com.interview.ai_prep_platform.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    private final UserRepository userRepository;

    public AppConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 1. RestTemplate Bean define karein
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // 2. UserDetailsService Bean
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                .map(user -> org.springframework.security.core.userdetails.User.builder()
                        .username(user.getEmail())
                        .password(user.getPassword())
                        .roles("USER")
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}