package com.cake.jwt;

import javax.crypto.SecretKey;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.google.common.net.HttpHeaders;

import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@ConfigurationProperties(prefix="application.jwt")
public class JwtConfig {
	
	private String secretKey;
	private String tokenPrefix;
	private int	tokenExpirationAfterDays;
	
	
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	public String getTokenPrefix() {
		return tokenPrefix;
	}
	public void setTokenPrefix(String tokenPrefix) {
		this.tokenPrefix = tokenPrefix;
	}
	public int getTokenExpirationAfterDays() {
		return tokenExpirationAfterDays;
	}
	public void setTokenExpirationAfterDays(int tokenExpirationAfterDays) {
		this.tokenExpirationAfterDays = tokenExpirationAfterDays;
	}
	
	@Bean
	public SecretKey getSecretKeyForSigning() {
		return Keys.hmacShaKeyFor(secretKey.getBytes());
	}
	
	public String getAuthorizationHeader() {
		return HttpHeaders.AUTHORIZATION;
	}
}
