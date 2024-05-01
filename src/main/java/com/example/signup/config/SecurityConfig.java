package com.example.signup.config;

import com.example.signup.config.auth.*;
import com.example.signup.config.jwt.JwtAuthenticationFilter;
import com.example.signup.config.jwt.JwtProvider;
import com.example.signup.config.oauth.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
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
    private final JwtProvider jwtProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(CsrfConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
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
                                .successHandler(new SignInSuccess(jwtProvider))
                                .failureHandler(new SignInFail()))
                .oauth2Login(httpSecurityOAuth2LoginConfigurer ->
                        httpSecurityOAuth2LoginConfigurer
                                .loginPage("/signin")
                                .defaultSuccessUrl("/home")
                                .successHandler(new SignInSuccess(jwtProvider))
                                .userInfoEndpoint(userInfoEndpointConfig ->
                                        userInfoEndpointConfig
                                                .userService(customOAuth2UserService)))
                .logout(httpSecurityLogoutConfigurer ->
                        httpSecurityLogoutConfigurer
                                .logoutUrl("/signout")
                                .logoutSuccessUrl("/home")
                                .deleteCookies(JwtProvider.AUTHORIZATION_HEADER)
                                .deleteCookies(JwtProvider.REFRESH_HEADER)
                                .logoutSuccessHandler(new SignOutSuccess()))
                .httpBasic(AbstractHttpConfigurer::disable)
                .exceptionHandling(ExceptionHandlingConfigurer ->
                        ExceptionHandlingConfigurer
                                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                                .accessDeniedHandler(new CustomAccessDeniedHandler()))
                .userDetailsService(customUserDetailsService)
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}