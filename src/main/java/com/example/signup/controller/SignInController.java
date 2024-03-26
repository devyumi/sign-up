package com.example.signup.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("signin")
@RequiredArgsConstructor
public class SignInController {

    @GetMapping
    public String signinOwn(@RequestParam(value = "error", required = false) String errorMessage, Model model) {
        model.addAttribute("errorMessage", errorMessage);
        return "signinForm";
    }
}