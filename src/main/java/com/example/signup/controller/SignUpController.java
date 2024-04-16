package com.example.signup.controller;

import com.example.signup.dto.MemberSignupDto;
import com.example.signup.service.SignUpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("signup")
@RequiredArgsConstructor
public class SignUpController {
    private final SignUpService signUpService;
    private final Logger logger = LoggerFactory.getLogger(SignUpController.class);

    @GetMapping
    public String signupOwn() {
        return "signupForm";
    }

    @PostMapping
    public String signupOwn(@Valid MemberSignupDto memberSignupDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            Map<String, String> errorMessage = new HashMap<>();

            for (FieldError fieldError : fieldErrors) {
                logger.error("{}: {}", fieldError.getField(), fieldError.getDefaultMessage());
                errorMessage.put(fieldError.getField(), fieldError.getDefaultMessage());

            }
            model.addAttribute("errorMessage", errorMessage);
            return "signupForm";
        }

        signUpService.signupOwn(memberSignupDto);
        logger.info("회원가입(자체) 완료: {}", memberSignupDto.getEmail());
        return "redirect:/home";
    }
}