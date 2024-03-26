package com.example.signup.config.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SignInSuccess implements AuthenticationSuccessHandler {
    private final Logger log = LoggerFactory.getLogger(SignInSuccess.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("자체 로그인 성공");
        log.info("username: {}", authentication.getName());
        log.info("authorities: {}", authentication.getAuthorities());
        response.sendRedirect("home");
    }
}