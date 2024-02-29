package com.example.signup.controller;

import com.example.signup.dto.MemberSignupDto;
import com.example.signup.service.SignUpService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("signup")
@AllArgsConstructor
public class SignUpController {
    private final SignUpService signUpService;
    private final Logger logger = LoggerFactory.getLogger(SignUpController.class);

    @PostMapping
    public ResponseEntity<MemberSignupDto> signupOwn(@RequestBody @Valid MemberSignupDto memberSignupDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                logger.error("{}: {}", fieldError.getField(), fieldError.getDefaultMessage());
            }
            return ResponseEntity.badRequest()
                    .body(MemberSignupDto.builder().build());
        }

        signUpService.signupOwn(memberSignupDto);
        return ResponseEntity.ok()
                .body(memberSignupDto);
    }

    @GetMapping("kakao")
    public ResponseEntity<String> signUpKakao(@RequestParam String code) {
        logger.info("code = {}", code);
        String accessToken = signUpService.getKakaoAccessToken(code);
        Map<String, String> userInfo = signUpService.getUserInfo(accessToken);

        return ResponseEntity.ok("성공");
    }
}
