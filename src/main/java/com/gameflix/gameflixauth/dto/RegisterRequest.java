package com.gameflix.gameflixauth.dto;

import jakarta.validation.constraints.Email;  // checks if a string looks like an email address
import jakarta.validation.constraints.NotBlank;  // string cannot be empty or spaces
import jakarta.validation.constraints.Size;  // limits the length of password

// Data Transfer Object - holds and validates the data sent when a new user registers
public class RegisterRequest
{
    @NotBlank  // field cannot be empty
    private String name;  // stores user's name

    @NotBlank
    @Email // email cannot be empty; must be valid
    private String email;  // stores user's email

    @NotBlank
    @Size(max = 64)
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}