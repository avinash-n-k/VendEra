package com.auth_service.security;

import java.util.Collection;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.auth_service.entity.User;
import com.auth_service.exception.UserNotFoundException;
import com.auth_service.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomerDetailsService implements UserDetailsService {
	
	private UserRepository userRepo; 

	@Override
	public UserDetails loadUserByUsername(String email) {
		
		User userdetails=userRepo.findByEmail(email).orElseThrow(()->new UserNotFoundException("Invalid Credentials"));
		return new UserDetails() {
			
			@Override
			public String getUsername() {
				
				return userdetails.getEmail();
			}
			
			@Override
			public @Nullable String getPassword() {
				
				return userdetails.getPassword();
			}
			
			@Override
			public Collection<? extends GrantedAuthority> getAuthorities() {
				
				return List.of(new SimpleGrantedAuthority("ROLE_"+userdetails.getRole()));
			}
		};
		
	
	}

}
