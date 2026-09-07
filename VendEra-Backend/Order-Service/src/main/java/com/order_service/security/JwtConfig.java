package com.order_service.security;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import io.jsonwebtoken.security.Keys;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String secretkey;

    @Bean
    public JwtDecoder jwtDecoder() {

        SecretKey key = Keys.hmacShaKeyFor(secretkey.getBytes());

        return NimbusJwtDecoder
                .withSecretKey(key)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }
}