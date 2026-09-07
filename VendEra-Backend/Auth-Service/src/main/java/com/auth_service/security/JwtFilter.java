package com.auth_service.security;



import java.io.IOException;
import java.time.LocalDateTime;


import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import com.auth_service.dto.ErrorResponse;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter{
	
	private JwtService jwtService;
	private CustomerDetailsService customerDetailsService;
	private ObjectMapper objectMapper;	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
	
	
	String header=request.getHeader("Authorization");
	
	if(header==null || header.startsWith("Bearer ")==false)
	{
		filterChain.doFilter(request, response);
		return;
	}
	
	
	try
	{
		
	
	
	String token=header.substring(7);
	String email=jwtService.extractEmail(token);

	
	if(email!=null && SecurityContextHolder.getContext().getAuthentication()==null)
	{
		UserDetails userDetails=customerDetailsService.loadUserByUsername(email);
		if(jwtService.isTokenValid(token, userDetails))
		{
			UsernamePasswordAuthenticationToken auth=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(auth);
		}
		filterChain.doFilter(request, response);
		return;
	}
	
	
	}
	catch(ExpiredJwtException e)
	{
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
		
		ErrorResponse error=new ErrorResponse("JWT Token Expired",HttpStatus.UNAUTHORIZED.value(),LocalDateTime.now());
		
		
		response.getWriter().write(objectMapper.writeValueAsString(error));
		return;
	}
	catch(Exception e)
	{
		e.printStackTrace();
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
		
		ErrorResponse error=new ErrorResponse("Invalid Jwt Token",HttpStatus.UNAUTHORIZED.value(),LocalDateTime.now());
		
		
		
		response.getWriter().write(objectMapper.writeValueAsString(error));
		return;
	}
	
	
	
	}
}


