package com.example.signup.config.auth;

import com.example.signup.config.jwt.JwtProvider;
import com.example.signup.service.TokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SignInSuccess extends SimpleUrlAuthenticationSuccessHandler {
    private final TokenService tokenService;
    private final JwtProvider jwtProvider;
    private final Logger log = LoggerFactory.getLogger(SignInSuccess.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomUserDetails auth = (CustomUserDetails) authentication.getPrincipal();

        log.info("로그인 성공");
        log.info("username: {} | authorities: {}", auth.getUsername(), auth.getAuthorities());

        String accessToken = jwtProvider.createToken(auth.getUsername(), getAuthorities(auth.getAuthorities()), JwtProvider.ACCESS_EXPIRATION_TIME);
        String refreshToken = jwtProvider.createToken(auth.getUsername(), getAuthorities(auth.getAuthorities()), JwtProvider.REFRESH_EXPIRATION_TIME);
        tokenService.saveToken(auth.getUsername(), refreshToken);
        log.info("Access Token: {}", accessToken);
        log.info("Refresh Token: {}", refreshToken);

        response.addCookie(createCookie(JwtProvider.AUTHORIZATION_HEADER, accessToken));
        response.addCookie(createCookie(JwtProvider.REFRESH_HEADER, refreshToken));
        response.sendRedirect("/home");
    }

    public static Cookie createCookie(String key, String value) throws UnsupportedEncodingException {
        Cookie jwtCookie = new Cookie(key, URLEncoder.encode(value, "UTF-8"));
        jwtCookie.setMaxAge(24 * 60 * 60);
        jwtCookie.setPath("/");
        jwtCookie.setHttpOnly(true);
        return jwtCookie;
    }

    private static String getAuthorities(Collection<? extends GrantedAuthority> role) {
        return role.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }
}