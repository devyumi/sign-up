package com.example.signup.config.oauth;

import com.example.signup.repository.MemberRepository;
import com.example.signup.repository.MemberRoleRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final Logger log = LoggerFactory.getLogger(CustomOAuth2UserService.class);

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info("oAuth2User: {}", oAuth2User.getName());

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        log.info("registration: {}", registrationId);

        if (registrationId.equals("kakao")) {

        } else {
            return null;
        }
        //추후 작성
        return oAuth2User;
    }
}
