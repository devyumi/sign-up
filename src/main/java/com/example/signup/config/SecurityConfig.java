package com.example.signup.config;

import com.example.signup.config.auth.*;
import com.example.signup.config.jwt.JwtAuthenticationFilter;
import com.example.signup.config.jwt.JwtProvider;
import com.example.signup.config.oauth.CustomOAuth2UserService;
import com.example.signup.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@EnableWebSecurity
@Component
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final SignInSuccess signInSuccess;
    private final SignOutSuccess signOutSuccess;
    private final SignInFail signInFail;
    private final TokenService tokenService;
    private final JwtProvider jwtProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(CsrfConfigurer::disable)

                .sessionManagement(sessionConfigurer -> sessionConfigurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(authorization -> authorization
                        .requestMatchers("/signup/**", "/signin/**").permitAll()
                        .requestMatchers("/search/members").hasRole("ADMIN")
                        .requestMatchers("/guestbook/**").hasAnyRole("ADMIN", "USER")
                        .anyRequest().permitAll())

                .formLogin(loginConfigurer -> loginConfigurer
                        .loginPage("/signin")
                        .loginProcessingUrl("/signin")
                        .defaultSuccessUrl("/home")
                        .successHandler(signInSuccess)
                        .failureHandler(signInFail))

                .oauth2Login(oAuth2LoginConfigurer -> oAuth2LoginConfigurer
                        .loginPage("/signin")
                        .defaultSuccessUrl("/home")
                        .successHandler(signInSuccess)
                        .userInfoEndpoint(userInfoEndpointConfig ->
                                userInfoEndpointConfig
                                        .userService(customOAuth2UserService)))

                .logout(logoutConfigurer -> logoutConfigurer
                        .logoutUrl("/signout")
                        .logoutSuccessUrl("/home")
                        .deleteCookies(JwtProvider.AUTHORIZATION_HEADER)
                        .deleteCookies(JwtProvider.REFRESH_HEADER)
                        .logoutSuccessHandler(signOutSuccess))

                .httpBasic(AbstractHttpConfigurer::disable)

                .exceptionHandling(ExceptionHandlingConfigurer ->
                        ExceptionHandlingConfigurer
                                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                                .accessDeniedHandler(new CustomAccessDeniedHandler()))

                .userDetailsService(customUserDetailsService)
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider, tokenService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}