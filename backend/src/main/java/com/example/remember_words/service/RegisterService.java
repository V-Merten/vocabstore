package com.example.remember_words.service;

import java.util.logging.Logger;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.remember_words.entity.User;
import com.example.remember_words.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class RegisterService {

    private final Logger logger = Logger.getLogger(RegisterService.class.getName());
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registerUser(String username, String email, String password) {

        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username '" + username + "' is already taken");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email " + email + " is already in use");
        }

        String EncodedPassword = passwordEncoder.encode(password);
     
        User user = new User(username, EncodedPassword, email);

        userRepository.save(user);

        logger.info("Registered new user | Username: " + username + " | Email: " + email);
    }

}
