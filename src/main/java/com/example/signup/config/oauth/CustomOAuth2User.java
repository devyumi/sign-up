package com.example.signup.config.oauth;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOAuth2User implements OAuth2User {
    private String username;
    private String name;
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return name;
    }

    @Builder
    public CustomOAuth2User(String username, String name, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.name = name;
        this.authorities = authorities;
    }
}