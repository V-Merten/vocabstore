package com.example.remember_words.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.remember_words.entity.PasswordResetTokens;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokens, Long> {
    
    Optional<PasswordResetTokens> findByToken(String token);

}
