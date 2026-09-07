package com.auth_service.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth_service.dto.LoginResponse;
import com.auth_service.dto.SuccessResponse;
import com.auth_service.dto.UserLoginRequest;
import com.auth_service.dto.UserRegisterRequest;
import com.auth_service.entity.RefreshToken;
import com.auth_service.entity.User;
import com.auth_service.exception.EmailAlreadyExistsException;
import com.auth_service.exception.InvalidCredentialsException;
import com.auth_service.exception.PasswordAndConfirmPasswordMissMatchException;
import com.auth_service.exception.RefreshTokenExpiredException;
import com.auth_service.exception.UserNotFoundException;
import com.auth_service.repository.RefreshTokenRepository;
import com.auth_service.repository.UserRepository;
import com.auth_service.security.CustomerDetailsService;
import com.auth_service.security.JwtService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
	
	private UserRepository userRepo;
	private PasswordEncoder passwordEncoder;
	private AuthenticationManager authenticationManager;
	private JwtService jwtService;
	private RefreshTokenRepository refreshTokenRepo;
	private CustomerDetailsService customerDetailsService;
	
	
	public ResponseEntity<SuccessResponse> userRegister(UserRegisterRequest userRegister)
	{
		if(userRepo.existsByEmail(userRegister.getEmail()))
		{
			throw new EmailAlreadyExistsException("Email Already Exists");
		}
		if(userRegister.getPassword().equals(userRegister.getConfirmPassword()))
		{
			User user=new User();
			user.setEmail(userRegister.getEmail());
			user.setPassword(passwordEncoder.encode(userRegister.getPassword()));
			user.setRole("USER");
			
			userRepo.save(user);
			
			SuccessResponse response=new SuccessResponse("Registered SuccessFully",HttpStatus.OK.value(), LocalDateTime.now());
			return new ResponseEntity<SuccessResponse>(response, HttpStatus.OK);
			
		}
		else
		{
			throw new PasswordAndConfirmPasswordMissMatchException("Password MissMatch");
		}
		
	}
	
	
	public ResponseEntity<SuccessResponse> userLogin(UserLoginRequest userLogin,HttpServletResponse response)
	{
		try
		{
		UsernamePasswordAuthenticationToken token=new UsernamePasswordAuthenticationToken(userLogin.getEmail(),userLogin.getPassword());
		
		Authentication authentication=authenticationManager.authenticate(token);
		System.out.println(authentication.isAuthenticated());
		User user=userRepo.findByEmail(userLogin.getEmail()).orElseThrow(()->new UserNotFoundException("User Doesn't Exist"));
		
		String accessToken=jwtService.generateAccessToken(userLogin.getEmail(),user.getRole());
		
		
		
		
		
		
		String refreshToken=jwtService.generateRefreshToken(userLogin.getEmail(),user.getRole());
		
		System.out.println("After Login RefreshToken: "+refreshToken);
		
		
		
		Optional<RefreshToken> oldRefreshToken=refreshTokenRepo.findByUserId(user.getId());
		
		if(oldRefreshToken.isPresent())
		{
			RefreshToken existingrefreshtoken=oldRefreshToken.get();
			existingrefreshtoken.setToken(refreshToken);
			existingrefreshtoken.setExpiryDate(LocalDateTime.now().plusHours(24));
			refreshTokenRepo.save(existingrefreshtoken);
		}
		else
		{
			
		
		
		RefreshToken newrefreshTokenn=new RefreshToken();
		newrefreshTokenn.setToken(refreshToken);
		newrefreshTokenn.setUser(user);
		newrefreshTokenn.setExpiryDate(LocalDateTime.now().plusHours(24));
		
		
		refreshTokenRepo.save(newrefreshTokenn);
		}
		
		
		ResponseCookie cookie=ResponseCookie.from("refreshToken",refreshToken)
				.httpOnly(true)
			    .secure(false)      // localhost
			    .sameSite("Lax")
			    .path("/")
			    .maxAge(Duration.ofHours(24))
			    .build();
		
		response.addHeader("Set-Cookie", cookie.toString());
		
		
		LoginResponse loginResponse=new LoginResponse();
		loginResponse.setAccessToken(accessToken);
		loginResponse.setRole(user.getRole());
		
		SuccessResponse successresponse=new SuccessResponse("Login SuccessFull",loginResponse,HttpStatus.OK.value(),LocalDateTime.now());
		return new ResponseEntity<SuccessResponse>(successresponse,HttpStatus.OK);
		}
		catch(BadCredentialsException ex)
		{
			throw new InvalidCredentialsException("Invalid Credentials");
		}
		
	}
	
	
	
	public ResponseEntity<SuccessResponse> refershToken(HttpServletRequest request,HttpServletResponse resposne)
	{
		Cookie[] cookies=request.getCookies();
		String refreshToken=null;
		if(cookies!=null)
		{
			for(Cookie cookie:cookies)
			{
				if(cookie.getName().equals("refreshToken"))
				{
					refreshToken=cookie.getValue();
					break;
				}
			}	
		}
		if(refreshToken==null)
		{
			throw new RefreshTokenExpiredException("Refresh Token Expired");
		}
		
		RefreshToken token=refreshTokenRepo.findByToken(refreshToken).orElseThrow(()->new RefreshTokenExpiredException("Refresh Token Not Found"));
		
		if(token.getExpiryDate().isAfter(LocalDateTime.now())==true)
		{
			String email=jwtService.extractEmail(refreshToken);
			UserDetails userDetails=customerDetailsService.loadUserByUsername(email);
			User user=userRepo.findByEmail(email).orElseThrow(()->new UserNotFoundException("User Not Found"));
			
			if(jwtService.isTokenValid(refreshToken, userDetails))
			{
				String newAccessToken=jwtService.generateAccessToken(email,user.getRole());
				String newRefreshToken=jwtService.generateRefreshToken(email,user.getRole());
				
				token.setToken(newRefreshToken);
				token.setExpiryDate(LocalDateTime.now().plusHours(24));
				
				refreshTokenRepo.save(token);
				
				
				ResponseCookie cookie=ResponseCookie.from("refreshToken",newRefreshToken)
						.httpOnly(true)
						.secure(false)
						.path("/")
						.sameSite("Lax")
						.maxAge(Duration.ofHours(24))
						.build();
				
				resposne.addHeader("Set-Cookie", cookie.toString());
				
				LoginResponse loginResponse=new LoginResponse();
				loginResponse.setAccessToken(newAccessToken);
				loginResponse.setRole(user.getRole());
				
				SuccessResponse successresponse=new SuccessResponse("Login SuccessFull",loginResponse,HttpStatus.OK.value(),LocalDateTime.now());
				return new ResponseEntity<SuccessResponse>(successresponse,HttpStatus.OK);
				
			
			}
		}
		
		refreshTokenRepo.delete(token);
		throw new RefreshTokenExpiredException("Refresh Token Expired");
		
		
	}
	
	
	
	public ResponseEntity<SuccessResponse> logout(HttpServletRequest request,HttpServletResponse response)
	{
		Cookie[] cookies=request.getCookies();
		String refreshToken=null;
		
		if(cookies!=null)
		{
			for(Cookie cookie:cookies)
			{
				if(cookie.getName().equals("refreshToken"))
				{
					refreshToken=cookie.getValue();
					break;
				}
			}
		}
		
		if(refreshToken==null)
		{
			throw new RefreshTokenExpiredException("Refresh Token Expired");
		}
		
		System.out.println("After Logout Refresh Token "+refreshToken);
		RefreshToken token=refreshTokenRepo.findByToken(refreshToken).orElseThrow(()->new RefreshTokenExpiredException("Refresh Token Not Found"));
		
		refreshTokenRepo.delete(token);
		
		ResponseCookie cookie=ResponseCookie.from("refreshToken","")
				.httpOnly(true)
				.secure(false)
				.path("/")
				.sameSite("Lax")
				.maxAge(0)
				.build();
		
		response.addHeader("Set-Cookie", cookie.toString());
		
		
		
		SuccessResponse success=new SuccessResponse("Logout SuccessFull", HttpStatus.OK.value(), LocalDateTime.now());
		
		return new ResponseEntity<SuccessResponse>(success,HttpStatus.OK);
		
	}

}
