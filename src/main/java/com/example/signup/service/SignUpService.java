package com.example.signup.service;

import com.example.signup.domain.Member;
import com.example.signup.domain.MemberRole;
import com.example.signup.dto.MemberSignupDto;
import com.example.signup.repository.MemberRepository;
import com.example.signup.repository.MemberRoleRepository;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SignUpService {
    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(SignUpService.class);

    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.client.secret}")
    private String clientSecret;

    public Long signupOwn(MemberSignupDto memberSignupDto) {
        checkDuplicationMember(memberSignupDto);
        comparePassword(memberSignupDto);

        Member member = Member.builder()
                .platformType("OWN")
                .email(memberSignupDto.getEmail())
                .password(passwordEncoder.encode(memberSignupDto.getPassword()))
                .nickname(memberSignupDto.getNickname())
                .build();

        memberRepository.save(member);

        MemberRole memberRole = MemberRole.builder()
                .roleName("ROLE_USER")
                .member(member)
                .build();

        return memberRoleRepository.save(memberRole).getId();
    }

    private void checkDuplicationMember(MemberSignupDto memberSignupDto) {
        memberRepository.findByEmail(memberSignupDto.getEmail())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    private void comparePassword(MemberSignupDto memberSignupDto) {
        if (!memberSignupDto.getPassword().equals(memberSignupDto.getCheckedPassword())) {
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }
    }

    public String getKakaoAccessToken(String code) {
        String accessToken = "";
        String refreshToken;
        String requestUrl = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(requestUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
            connection.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            StringBuilder sb = new StringBuilder();

            sb.append("grant_type=authorization_code");
            sb.append("&client_id=").append(clientId);
            sb.append("&redirect_uri=").append(redirectUri);
            sb.append("&code=").append(code);
            sb.append("&client_secret=").append(clientSecret);
            bw.write(sb.toString());
            bw.close();

            logger.info("responseCode: {}", connection.getResponseCode());

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder result = new StringBuilder();

            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            logger.info("response body = {}", result);

            JsonObject object = (JsonObject) JsonParser.parseString(result.toString());

            accessToken = object.getAsJsonObject().get("access_token").getAsString();
            refreshToken = object.getAsJsonObject().get("refresh_token").getAsString();

            logger.info("access_token = {}", accessToken);
            logger.info("refresh_token = {}", refreshToken);
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accessToken;
    }

    public Map<String, String> getUserInfo(String accessToken) {
        Map<String, String> userInfo = new HashMap<>();
        String requestUrl = "https://kapi.kakao.com/v2/user/me";

        try {
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);
            connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
            connection.setDoOutput(true);

            logger.info("UserInfo responseCode: {}", connection.getResponseCode());

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder result = new StringBuilder();

            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            logger.info("response body = {}", result);

            JsonObject object = (JsonObject) JsonParser.parseString(result.toString());
            String email = object.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();
            String nickname = object.getAsJsonObject().get("kakao_account").getAsJsonObject().get("profile").getAsJsonObject().get("nickname").getAsString();

            userInfo.put("email", email);
            userInfo.put("nickname", nickname);
            logger.info("email = {}", email);
            logger.info("nickname = {}", nickname);
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return userInfo;
    }
}
