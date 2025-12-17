package com.example.remember_words.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SpaController {

    @RequestMapping(value = {
      "/register",
      "/forgot-password",
      "/reset-password",
      "/home",
      "/practice"
  })
    public String forwardToSpa() {
        return "forward:/index.html";
    }
}
