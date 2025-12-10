package com.example.remember_words.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaController {

    @GetMapping({
        "/",
        "/register",
        "/forgot-password",
        "/reset-password"
    })
    public String forwardToSpa() {
        return "forward:/index.html";
    }
}
