package com.example.signup.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class SignInController {
    @Value("${oauth.kakao.client-id}")
    private String clientId;

    @Value("${oauth.kakao.url.redirect}")
    private String redirectUri;

    @GetMapping("signin")
    public String signin(Model model) {
        model.addAttribute("REST_API_KEY", clientId);
        model.addAttribute("REDIRECT_URI", redirectUri);
        return "signinForm";
    }

    @PostMapping("signout")
    public String signout() {
        return "home";
    }
}