package com.example.remember_words.service;

import java.util.logging.Logger;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.remember_words.entity.User;
import com.example.remember_words.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class AccountService {

    private final Logger logger = Logger.getLogger(AccountService.class.getName());
    private final CurrentUserService currentUserService;
    private final UserRepository userRepository;

    public AccountService(CurrentUserService currentUserService,
                          UserRepository userRepository) {
        this.currentUserService = currentUserService;
        this.userRepository = userRepository;
    }

    @Transactional
    public void deleteAccount() {
        String user = currentUserService.getCurrentUsername();
        User existingUser = userRepository.findByUsername(user)
             .orElseThrow(() -> new RuntimeException("User not found"));

        logger.info("Initializing account deletion. User: " + user);
        userRepository.delete(existingUser);
        logger.info("Account deletion completed");
        SecurityContextHolder.clearContext();
    }
}
