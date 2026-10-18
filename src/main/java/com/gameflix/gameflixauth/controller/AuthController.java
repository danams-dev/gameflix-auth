package com.gameflix.gameflixauth.controller;

import java.util.Map;
import com.gameflix.gameflixauth.dto.RegisterRequest;
import com.gameflix.gameflixauth.dto.UserResponse;
import com.gameflix.gameflixauth.dto.LoginRequest;
import com.gameflix.gameflixauth.model.User;
import com.gameflix.gameflixauth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Handles authentication API requests, such as user registration
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        User createdUser = authService.register(request);

        // converts saved user into safe response so the password hash is never returned by the API
        UserResponse response = new UserResponse(
                createdUser.getUserId(),
                createdUser.getName(),
                createdUser.getEmail(),
                createdUser.getRole(),
                createdUser.getSubscriptionStatus()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)  // returns "201 Created"
                .body(response);
    }

    // creates login POST endpoint
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {

        User user = authService.authenticate(request);

        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED) // returns 401
                    .body("Invalid email or password"); // does not reveal whether email/pass was incorrect
        }

        String token = authService.generateToken(user); // calls generateToken() in AuthService to create the signed JWT

        return ResponseEntity.ok(Map.of("token", token));  // returns 200 OK with the JWT in a JSON object
    }
}