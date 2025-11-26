package com.example.remember_words.controller;

import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.remember_words.dto.UserDto;
import com.example.remember_words.service.RegisterService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class RegisterController {

    private final Logger logger = Logger.getLogger(RegisterController.class.getName());
    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }
    
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserDto userDto) {
        try{
        registerService.registerUser(userDto.getUsername(), 
                                    userDto.getEmail(),
                                    userDto.getPassword());

        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (IllegalArgumentException e){
            logger.warning("Registration failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
