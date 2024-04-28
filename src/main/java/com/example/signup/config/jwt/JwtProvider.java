package com.example.signup.config.jwt;

import com.example.signup.config.auth.CustomUserDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtProvider {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTHORITIES_KEY = "auth";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;
    private final Key key;

    private final CustomUserDetailsService customUserDetailsService;
    private final Logger log = LoggerFactory.getLogger(JwtProvider.class);

    public JwtProvider(@Value("${jwt.secret}") String secretKey, CustomUserDetailsService customUserDetailsService) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.customUserDetailsService = customUserDetailsService;
    }

    public String createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");

        return BEARER_PREFIX +
                Jwts.builder()
                        .setHeader(header)
                        .setSubject(authentication.getName())
                        .claim(AUTHORITIES_KEY, authorities)
                        .setExpiration(new Date(new Date().getTime() + ACCESS_TOKEN_EXPIRE_TIME))
                        .signWith(key, SignatureAlgorithm.HS256)
                        .compact();
    }

    public String resolveToken(Cookie[] cookies) {
        String bearerToken = "";

        if (cookies != null) {
            for (Cookie c : cookies) {
                String name = c.getName().toString();
                if (name.equals(AUTHORIZATION_HEADER.toString())) {
                    bearerToken = c.getValue();
                }
            }
        }

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX.replace(" ", "+"))) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 서명입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않은 JWT 서명입니다.");
        } catch (IllegalArgumentException e) {
            log.error("잘못된 JWT 서명입니다.");
        }
        return false;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        UserDetails principal = customUserDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
    }
}