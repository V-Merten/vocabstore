package com.example.remember_words.controller;

import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.remember_words.dto.LoginDto;
import com.example.remember_words.service.LoginService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class LoginController {

    private final Logger logger = Logger.getLogger(LoginController.class.getName());
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginDto loginDto, HttpServletRequest request) {
        try{
            loginService.loginUser(loginDto.getUsername(), loginDto.getPassword(), request);
            return ResponseEntity.ok("Login successful");
        } catch (BadCredentialsException e) {
            logger.warning("Failed login attempt | Username: " + loginDto.getUsername() + " | Reason: Bad credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }
    
}
