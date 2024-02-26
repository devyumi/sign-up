package com.example.signup.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class SignUpService {
    private final Logger logger = LoggerFactory.getLogger(SignUpService.class);

    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.client.secret}")
    private String clientSecret;

    public String getKakaoAccessToken(String code) {
        String accessToken = "";
        String refreshToken;
        String requestURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(requestURL);

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
}
