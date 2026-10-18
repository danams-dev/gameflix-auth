package com.gameflix.gameflixauth.service;

import com.gameflix.gameflixauth.dto.RegisterRequest;
import com.gameflix.gameflixauth.model.User;
import com.gameflix.gameflixauth.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

// Handles user registration logic for GameFlix.
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(RegisterRequest request) {

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User(
                request.getName(),
                request.getEmail(),
                hashedPassword,
                "USER",
                "INACTIVE",
                LocalDateTime.now()
        );

        return userRepository.save(user);
    }
}