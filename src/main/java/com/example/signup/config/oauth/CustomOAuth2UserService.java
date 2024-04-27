package com.example.signup.config.oauth;

import com.example.signup.domain.Member;
import com.example.signup.domain.MemberRole;
import com.example.signup.dto.GoogleResponse;
import com.example.signup.dto.KakaoResponse;
import com.example.signup.dto.NaverResponse;
import com.example.signup.dto.OAuth2Response;
import com.example.signup.repository.MemberRepository;
import com.example.signup.repository.MemberRoleRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final Logger log = LoggerFactory.getLogger(CustomOAuth2UserService.class);

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response;

        if (registrationId.equals("kakao")) {
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else if(registrationId.equals("google")){
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else {
            log.error("OAuth2 로그인 실패");
            return null;
        }
        return createOAuth2User(createMember(oAuth2Response));
    }

    private Member createMember(OAuth2Response oAuth2Response) {
        String username = oAuth2Response.getProvider() + "_" + oAuth2Response.getEmail();
        Optional<Member> tmpMember = memberRepository.findByEmail(username);
        Member member;

        if (!tmpMember.isPresent()) {
            member = Member.builder()
                    .email(username)
                    .nickname(oAuth2Response.getNickname())
                    .build();
            memberRepository.save(member);
            memberRoleRepository.save(MemberRole.builder()
                    .roleName("ROLE_USER")
                    .member(member)
                    .build());
            return member;
        }
        return tmpMember.get();
    }

    private CustomOAuth2User createOAuth2User(Member member) {
        List<MemberRole> customRoles = memberRoleRepository.findAllByMemberId(member.getId());
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (!customRoles.isEmpty()) {
            for (MemberRole memberRole : customRoles) {
                authorities.add(new SimpleGrantedAuthority(memberRole.getRoleName()));
            }
        }
        return CustomOAuth2User.builder()
                .username(member.getEmail())
                .name(member.getNickname())
                .authorities(authorities)
                .build();
    }
}