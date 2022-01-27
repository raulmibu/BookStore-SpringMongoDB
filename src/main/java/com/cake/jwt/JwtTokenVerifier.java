package com.cake.jwt;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.common.base.Strings;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

// this filter is executed once per request
public class JwtTokenVerifier extends OncePerRequestFilter {

	private final JwtConfig jwtConfig;
	
	@Autowired
	public JwtTokenVerifier(JwtConfig jwtConfig) {
		this.jwtConfig = jwtConfig;
	}


//	we must get the token from the header to verify it
	@Override
	protected void doFilterInternal(HttpServletRequest request, 
			HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		
		String header=request.getHeader(jwtConfig.getAuthorizationHeader());//get the Authorization that contains the token
		if (Strings.isNullOrEmpty(header) || !header.startsWith(jwtConfig.getTokenPrefix())) {
//			if we're here the token is not there or is not valid
			filterChain.doFilter(request, response);
			return;
		}
		String token=header.replace(jwtConfig.getTokenPrefix(), "");
		try {
			@SuppressWarnings("deprecation")
			Jws<Claims> claimsJws=
			Jwts.parser()//parse the actual token
				.setSigningKey(jwtConfig.getSecretKeyForSigning())
				.parseClaimsJws(token);
			Claims body=claimsJws.getBody();//get the body
			String username=body.getSubject();//this is the username
			@SuppressWarnings("unchecked")
			List<Map<String, String>> authorities= (List<Map<String, String>>) body.get("authorities");//get the authorities
			
			Set<SimpleGrantedAuthority> simpleGrantedAuthorities= authorities.stream().map(m -> new SimpleGrantedAuthority(m.get("authority")))
				.collect(Collectors.toSet());//get the authorities as a set SimpleGrantedAuthority
			
			Authentication authentication=new UsernamePasswordAuthenticationToken(
					username, 
					null,
					simpleGrantedAuthorities
			);		
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}catch(JwtException e) {
			throw new IllegalStateException(String.format("Token %s cannot be trusted",token));
		}
		filterChain.doFilter(request, response);
	}

}
