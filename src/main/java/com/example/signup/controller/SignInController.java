package com.example.signup.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class SignInController {
    @GetMapping("signin")
    public String signinOwn() {
        return "signinForm";
    }

    @PostMapping("signout")
    public String signout() {
        return "home";
    }
}