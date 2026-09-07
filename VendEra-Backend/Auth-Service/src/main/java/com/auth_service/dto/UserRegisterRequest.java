package com.auth_service.dto;

import lombok.Data;

@Data
public class UserRegisterRequest {
	
	private String email;
	private String password;
	private String confirmPassword;
}
