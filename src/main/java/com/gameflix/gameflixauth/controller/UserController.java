package com.gameflix.gameflixauth.controller;

import com.gameflix.gameflixauth.dto.UserResponse;
import com.gameflix.gameflixauth.model.User;
import com.gameflix.gameflixauth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// handles protected user API requests, including returning
// the currently logged-in user's profile

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final AuthService authService;

    public UserController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(
            @AuthenticationPrincipal Jwt jwt) {

        String email = jwt.getSubject();
        User user = authService.findUserByEmail(email);

        UserResponse response = new UserResponse(
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getSubscriptionStatus()
        );

        return ResponseEntity.ok(response);
    }
}