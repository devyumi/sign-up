package com.example.signup.config.auth;

import com.example.signup.config.jwt.JwtProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;

@Component
@RequiredArgsConstructor
public class SignInSuccess implements AuthenticationSuccessHandler {
    private final JwtProvider jwtProvider;
    private final Logger log = LoggerFactory.getLogger(SignInSuccess.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String token = jwtProvider.createToken(authentication);

        log.info("자체 로그인 성공");
        log.info("username: {}", authentication.getName());
        log.info("authorities: {}", authentication.getAuthorities());
        log.info("token: {}", token.toString());

        Cookie jwtCookie = new Cookie(JwtProvider.AUTHORIZATION_HEADER, URLEncoder.encode(token, "UTF-8"));
        jwtCookie.setMaxAge(60 * 30);
        response.addCookie(jwtCookie);
        response.sendRedirect("home");
    }
}