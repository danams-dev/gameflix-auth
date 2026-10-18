package com.gameflix.gameflixauth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;

import java.time.Instant;

import com.gameflix.gameflixauth.dto.RegisterRequest;
import com.gameflix.gameflixauth.dto.LoginRequest;
import com.gameflix.gameflixauth.model.User;
import com.gameflix.gameflixauth.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

// Handles user registration and login logic for GameFlix
@Service
public class AuthService {

    // gives service access to database and password hashing
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtEncoder jwtEncoder;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtEncoder jwtEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    public User register(RegisterRequest request) {

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        // builds the database user
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

    // Checks whether the submitted login credentials match a stored user
    public User authenticate(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);

        // if the user does not exist or password does not match, login fails
        if (user == null ||
                !passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            return null;
        }

        return user;
    }

    // receives the logged in User and returns a JWT as a String
    public String generateToken(User user) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(user.getEmail())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(jwtExpiration))
                .claim("name", user.getName())
                .claim("role", user.getRole())
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

        return jwtEncoder
                .encode(JwtEncoderParameters.from(header, claims))
                .getTokenValue();
    }

    // takes an email and returns a User
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}