package com.example.remember_words.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.remember_words.service.AccountService;

@RestController
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/account/delete")
    public ResponseEntity<String> deleteAccount() {
        try{
            accountService.deleteAccount();
            return ResponseEntity.ok("Account deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body("Account deletion failed: " + e.getMessage());
        }
    }
}
