package com.cake.jwt;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;

// add the filter to validate credentials and send the jwt 
public class JwtUserAndPasswordFilter extends UsernamePasswordAuthenticationFilter{

	private final JwtConfig jwtConfig;
	
	private final AuthenticationManager authenticationManager;
	
//	we don't @Autowired this so we give this as a parameter from SecureConfig
	public JwtUserAndPasswordFilter(AuthenticationManager authenticationManager, JwtConfig jwtConfig) {
		this.authenticationManager=authenticationManager;
		this.jwtConfig=jwtConfig;
	}
	
//	override the attempt auth method, so we use jwt instead 
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, 
			HttpServletResponse response) throws AuthenticationException {
		try {
//			map the user and password from the http request to the UserPasswordAuthRequest object
			UserPasswordAuthRequest authRequest=
					new ObjectMapper().readValue(request.getInputStream(),UserPasswordAuthRequest.class);
			
//			use the user and password from the http request, 
			Authentication authentication=new UsernamePasswordAuthenticationToken(
					authRequest.getUsername(),//this is the principal 
					authRequest.getPassword());//the password
//			we send the authentication object, the manager will check the username and the password
//			with the authentication provider in the SecurityConfig
			return authenticationManager.authenticate(authentication);//we send the Authentication object
			
			
		}catch(IOException e) {
			throw new RuntimeException(e);
		}
	}

//	this method receives the FilterChain, we must pass the filter
//	we generate the token here and send it to the client
//	this method is executed after the attemptAuthentication, only if it is successful
	@Override
	protected void successfulAuthentication(HttpServletRequest request, 
			HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {

		String token=Jwts.builder()
			.setSubject(authResult.getName())//set the subject, use the user name
			.claim("authorities", authResult.getAuthorities())//set the body, set the authorities
			.setIssuedAt(new Date())
			.setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays())))//when the token expires
			.signWith(jwtConfig.getSecretKeyForSigning())//the key must be really long
			.compact();
		response.addHeader(jwtConfig.getAuthorizationHeader(), jwtConfig.getTokenPrefix()+token);//add the token to the header in the http response
		
	}	

	
}
