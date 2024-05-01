package com.example.signup.config.auth;

import com.example.signup.config.jwt.JwtProvider;
import com.example.signup.service.TokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SignOutSuccess implements LogoutSuccessHandler {
    private final JwtProvider jwtProvider;
    private final TokenService tokenService;
    private final Logger log = LoggerFactory.getLogger(SignOutSuccess.class);

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String refreshToken = jwtProvider.resolveToken(request.getCookies(), JwtProvider.REFRESH_HEADER);
        tokenService.deleteToken(refreshToken);
        log.info("로그아웃 완료");
        response.sendRedirect("home");
    }
}