package com.example.signup.service;

import com.example.signup.domain.RefreshToken;
import com.example.signup.repository.MemberRepository;
import com.example.signup.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long saveToken(String username, String token) {
        RefreshToken refreshToken = RefreshToken.builder()
                .member(memberRepository.findByEmail(username).get())
                .token(token.substring(7))
                .build();
        return refreshTokenRepository.save(refreshToken).getId();
    }

    @Transactional
    public void deleteToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

    public boolean isExistToken(String token) {
        return refreshTokenRepository.existsByToken(token);
    }
}