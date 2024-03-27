package com.example.signup.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("signin")
@RequiredArgsConstructor
public class SignInController {

    @GetMapping
    public String signinOwn() {
        return "signinForm";
    }
}