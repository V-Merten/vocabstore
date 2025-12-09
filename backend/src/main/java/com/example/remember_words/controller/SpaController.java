package com.example.remember_words.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaController {

    @GetMapping("/reset-password")
    public String resetPasswordPage() {
        return "forward:/index.html";
    }
}
