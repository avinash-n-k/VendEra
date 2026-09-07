package com.auth_service.security;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretkey;

    public String generateAccessToken(String email, String role) {
        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .signWith(getSignKey())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 300000))
                .compact();
    }

    public String generateRefreshToken(String email, String role) {
        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .signWith(getSignKey())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 8_64_00_000))
                .compact();
    }

    public SecretKey getSignKey() {
        byte[] keybytes = secretkey.getBytes();

        return Keys.hmacShaKeyFor(keybytes);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T> T extractClaims(String token, Function<Claims, T> resolver) {
        final Claims claim = extractAllClaims(token);

        return resolver.apply(claim);
    }

    public String extractEmail(String token) {
        return extractClaims(token, Claims -> Claims.getSubject());
    }

    public Date extractExpiration(String token) {
        return extractClaims(token, Claims -> Claims.getExpiration());
    }

    public Date extractIssuedAt(String token) {
        return extractClaims(token, Claims -> Claims.getIssuedAt());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String email = extractEmail(token);

        return email.equals(userDetails.getUsername()) && isTokenExpired(token) == false;
    }

    public boolean isTokenExpired(String token) {
        return new Date().after(extractExpiration(token));
    }
}