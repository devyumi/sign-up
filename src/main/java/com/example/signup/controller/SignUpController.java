package com.example.signup.controller;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("signup")
@AllArgsConstructor
public class SignUpController {
    private final Logger logger = LoggerFactory.getLogger(SignUpController.class);

    @GetMapping("kakao")
    public ResponseEntity<String> signUpKakao(@RequestParam String code) {
        logger.info(code);
        return ResponseEntity.ok("성공");
    }
}
