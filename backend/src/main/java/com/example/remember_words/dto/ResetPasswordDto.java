package com.example.remember_words.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ResetPasswordDto {

    @NotBlank
    private String token;
    
    @NotBlank
    @Size(min = 6, max = 50, message = "Password must be at least 6 characters long")
    private String newPassword;

    public ResetPasswordDto() {
    }

    public ResetPasswordDto(String token, String newPassword) {
        this.token = token;
        this.newPassword = newPassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
