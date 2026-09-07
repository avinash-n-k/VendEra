package com.api_gateway.security;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.api_gateway.dto.ErrorResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Component
@AllArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private ObjectMapper objectMapper;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        String message = "Invalid Jwt Token";

        if (authException.getMessage() != null &&
                authException.getMessage().toLowerCase().contains("expired")) {

            message = "JWT Token Expired";
        }

        ErrorResponse error =
                new ErrorResponse(
                        message,
                        HttpStatus.UNAUTHORIZED.value(),
                        LocalDateTime.now()
                );

        response.getWriter()
                .write(objectMapper.writeValueAsString(error));
    }
}