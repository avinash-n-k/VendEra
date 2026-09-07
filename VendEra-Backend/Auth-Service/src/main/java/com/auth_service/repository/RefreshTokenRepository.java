package com.auth_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auth_service.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>{
	
	
	Optional<RefreshToken> findByToken(String token);
	
	
	Optional<RefreshToken> findByUserId(Long id);

}