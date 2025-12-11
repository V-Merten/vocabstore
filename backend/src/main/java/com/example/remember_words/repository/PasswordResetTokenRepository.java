package com.example.remember_words.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.remember_words.entity.PasswordResetTokens;
import com.example.remember_words.entity.User;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokens, Long> {
    
    Optional<PasswordResetTokens> findByToken(String token);

    Optional<PasswordResetTokens> deleteAllByUser(User user);

}
