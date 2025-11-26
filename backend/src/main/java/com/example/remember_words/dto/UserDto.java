package com.example.remember_words.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserDto {

    @NotBlank(message = "Username cannot be empty")
    @Size(max = 50)
    private String username;

    @Email
    @NotBlank(message = "Email cannot be empty")
    @Size(max = 100)
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, max =50, message = "Password must be between 6 and 50 characters")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
