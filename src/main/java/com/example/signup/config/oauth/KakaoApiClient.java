package com.example.signup.config.oauth;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class KakaoApiClient {
    @Value("${oauth.kakao.client-id}")
    private String clientId;

    @Value("${oauth.kakao.secret}")
    private String secretKey;

    @Value("${oauth.kakao.url.auth}")
    private String authUrl;

    @Value("${oauth.kakao.url.api}")
    private String apiUrl;

    @Value("${oauth.kakao.url.redirect}")
    private String redirectUrl;
}
