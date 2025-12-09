package com.example.remember_words.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.remember_words.entity.PasswordResetTokens;
import com.example.remember_words.entity.User;
import com.example.remember_words.repository.PasswordResetTokenRepository;
import com.example.remember_words.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class PasswordResetService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;


    public PasswordResetService(UserRepository userRepository, EmailService emailService,
            PasswordResetTokenRepository tokenRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createPasswordResetToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No user found with email: " + email));

        String token = java.util.UUID.randomUUID().toString();

        PasswordResetTokens resetToken = new PasswordResetTokens(
                token,
                user,
                LocalDateTime.now().plusHours(1)
        );

        tokenRepository.save(resetToken);

        String resetLink = "http://localhost:8080/reset-password?token=" + token;
        emailService.sendPasswordResetEmail(user, resetLink);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetTokens resetToken = validateToken(token);

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetToken.setUsed(true);
        tokenRepository.save(resetToken);
    }

    public PasswordResetTokens validateToken(String token) {
        PasswordResetTokens resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (resetToken.isUsed())
            throw new RuntimeException("Token already used");
            
        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new RuntimeException("Token expired");
    
        return resetToken;
    }

    
}
