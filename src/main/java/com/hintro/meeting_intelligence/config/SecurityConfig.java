package com.hintro.meeting_intelligence.config;

import com.hintro.meeting_intelligence.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(
                                        "/api/v1/auth/**",
                                           "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/swagger-ui/index.html",
                                        "/v3/api-docs/**",
                                        "/api-docs/**",
                                        "/health",
                                        "/evaluation"
                                )
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                );

        http.addFilterBefore(
                jwtFilter,
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }
}
