package com.api_gateway.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of(
                        "http://localhost:5173",
                        "http://localhost:5174",
                        "http://vendera-frontend-2026-1318.s3-website.ap-south-1.amazonaws.com"));

        configuration.setAllowedMethods(
                List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        configuration.setAllowedHeaders(
                List.of("*"));

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        return http

                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/user/login",
                                "/user/register",
                                "/user/refresh",
                                "/user/logout")
                        .permitAll()

                        .requestMatchers("/admin/**")
                        .hasRole("ADMIN")

                        .requestMatchers(request -> request.getMethod().equals("OPTIONS"))
                        .permitAll()

                        .anyRequest().authenticated())

                .oauth2ResourceServer(oauth2 -> oauth2
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(
                                new JwtAuthenticationConverter())))

                .build();
    }
}