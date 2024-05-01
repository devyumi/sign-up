package com.example.signup.config.jwt;

import com.example.signup.config.auth.SignInSuccess;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtProvider.resolveToken(request.getCookies(), JwtProvider.AUTHORIZATION_HEADER);
        String refreshToken = jwtProvider.resolveToken(request.getCookies(), JwtProvider.REFRESH_HEADER);

        if (!StringUtils.hasText(accessToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!jwtProvider.validateToken(accessToken)) {
            if (jwtProvider.validateToken(refreshToken)) {
                accessToken = jwtProvider.createToken(jwtProvider.getUsername(refreshToken), jwtProvider.getAuthorities(refreshToken), JwtProvider.ACCESS_EXPIRATION_TIME);
                refreshToken = jwtProvider.createToken(jwtProvider.getUsername(refreshToken), jwtProvider.getAuthorities(refreshToken), JwtProvider.REFRESH_EXPIRATION_TIME);
                response.addCookie(SignInSuccess.createCookie(JwtProvider.AUTHORIZATION_HEADER, accessToken));
                response.addCookie(SignInSuccess.createCookie(JwtProvider.REFRESH_HEADER, refreshToken));
                log.info("Access Token 재발급");
                log.info("Refresh Token 재발급");

                Authentication authentication = jwtProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
            } else {
                filterChain.doFilter(request, response);
            }
        } else {
            Authentication authentication = jwtProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        }
    }
}