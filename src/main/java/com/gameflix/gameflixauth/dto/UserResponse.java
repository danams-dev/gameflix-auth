package com.gameflix.gameflixauth.dto;

// Data Transfer Object - holds safe user information returned by the API
public class UserResponse {

    private Long userId;
    private String name;
    private String email;
    private String role;
    private String subscriptionStatus;

    public UserResponse(Long userId, String name, String email,
                        String role, String subscriptionStatus) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
        this.subscriptionStatus = subscriptionStatus;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getSubscriptionStatus() {
        return subscriptionStatus;
    }
}