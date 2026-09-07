package com.auth_service.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.auth_service.dto.SuccessResponse;
import com.auth_service.dto.UserLoginRequest;
import com.auth_service.dto.UserRegisterRequest;
import com.auth_service.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
	
	private UserService userService;
	
	@PostMapping("/register")
	public ResponseEntity<SuccessResponse> userRegister(@RequestBody UserRegisterRequest userRegister){

		return userService.userRegister(userRegister);
		
	}
	
	@PostMapping("/login")
	public ResponseEntity<SuccessResponse> userLogin(@RequestBody UserLoginRequest userLogin,HttpServletResponse response)
	{
		return userService.userLogin(userLogin,response);
	}
	
	@GetMapping("/test")
	public String test() {
	    return "JWT Authentication Working";
	}
	
	@PostMapping("/refresh")
	public ResponseEntity<SuccessResponse> refreshToken(HttpServletRequest request,HttpServletResponse response)
	{
		return userService.refershToken(request, response);
	}
	
	
	@GetMapping("/logout")
	public ResponseEntity<SuccessResponse> logout(HttpServletRequest request,HttpServletResponse response)
	{
		
		
		return userService.logout(request, response);
 	}

}
