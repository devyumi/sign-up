package com.example.signup.controller;

import com.example.signup.config.oauth.KakaoApiClient;
import com.example.signup.service.SignInService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("signin")
@RequiredArgsConstructor
public class SignInController {
    private final KakaoApiClient kakaoApiClient;
    private final SignInService signInService;

    @GetMapping
    public String signin(Model model) {
        model.addAttribute("REST_API_KEY", kakaoApiClient.getClientId());
        model.addAttribute("REDIRECT_URI", kakaoApiClient.getRedirectUrl());
        return "signinForm";
    }

    @GetMapping("kakao")
    public String signinKakao(@RequestParam("code") String code) {
        String accessToken = signInService.getKakaoAccessToken(code);
        String userInfo = signInService.getUserInfo(accessToken);
        return "redirect:/home";
    }

    @PostMapping("signout")
    public String signout() {
        return "home";
    }
}