package com.auth_service.dto;

import lombok.Data;

@Data
public class LoginResponse {
	private String accessToken;
	private String role;
}
