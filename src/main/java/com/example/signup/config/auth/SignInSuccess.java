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
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Component
@RequiredArgsConstructor
public class SignInSuccess extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtProvider jwtProvider;
    private final Logger log = LoggerFactory.getLogger(SignInSuccess.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomUserDetails auth = (CustomUserDetails) authentication.getPrincipal();

        log.info("로그인 성공");
        log.info("username: {} | authorities: {}", auth.getUsername(), auth.getAuthorities());

        String token = jwtProvider.createToken(authentication);
        log.info("token {}", token);
        response.addCookie(createCookie(JwtProvider.AUTHORIZATION_HEADER, token));
        response.sendRedirect("/home");
    }

    private Cookie createCookie(String key, String value) throws UnsupportedEncodingException {
        Cookie jwtCookie = new Cookie(key, URLEncoder.encode(value, "UTF-8"));
        jwtCookie.setMaxAge(60 * 30);
        jwtCookie.setPath("/");
        jwtCookie.setHttpOnly(true);
        return jwtCookie;
    }
}