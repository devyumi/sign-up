package com.example.signup.config;

import com.example.signup.config.auth.*;
import com.example.signup.config.jwt.JwtAuthenticationFilter;
import com.example.signup.config.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
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
    private final SignInSuccess signInSuccess;
    private final SignInFail signInFail;
    private final SignOutSuccess signOutSuccess;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final JwtProvider jwtProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(CsrfConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry
                                .requestMatchers("/signup/**", "/signin/**").permitAll()
                                .requestMatchers("/search/members").hasRole("ADMIN")
                                .requestMatchers("/guestbook/**").hasAnyRole("ADMIN", "USER")
                                .anyRequest().permitAll())
                .formLogin(AbstractHttpConfigurer ->
                        AbstractHttpConfigurer
                                .loginPage("/signin")
                                .loginProcessingUrl("/signin")
                                .defaultSuccessUrl("/home")
                                .successHandler(signInSuccess)
                                .failureHandler(signInFail))
                .logout(httpSecurityLogoutConfigurer ->
                        httpSecurityLogoutConfigurer
                                .logoutUrl("/signout")
                                .logoutSuccessUrl("/home")
                                .logoutSuccessHandler(signOutSuccess))
                .httpBasic(AbstractHttpConfigurer::disable)
                .exceptionHandling(ExceptionHandlingConfigurer ->
                        ExceptionHandlingConfigurer
                                .authenticationEntryPoint(authenticationEntryPoint)
                                .accessDeniedHandler(accessDeniedHandler))
                .userDetailsService(customUserDetailsService)
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}