package com.cake;


import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cake.auth.ApplicationUserService;
import com.cake.jwt.JwtConfig;




@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)//enable pre authorize request
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
//	for dao auth provider
	
	private final ApplicationUserService applicationUserService;
		
	@Autowired
	public SecurityConfig(ApplicationUserService applicationUserService) {
		this.applicationUserService = applicationUserService;
	}


//	bean for password encoder wires
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
		return bCryptPasswordEncoder;
	}
	
	
	@Bean
	public JwtConfig jwtConfig() {
		return new JwtConfig();
	}
	
	//it uses the http security object
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http
				.csrf().disable()
//				//jwtfilters
//				.addFilter(new JwtUserAndPasswordFilter(authenticationManager(),jwtConfig()))//send the authentication manager to the filter
//				.addFilterAfter(new JwtTokenVerifier(jwtConfig()),JwtUserAndPasswordFilter.class)
//				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//jwt is stateless
//			.and()
				.authorizeRequests()
				.antMatchers("/","index","/api/**","/css/**","/images/**","/js/**","/login","/signup","/signingup","/book/**","/search/**","/categories/**")
				.permitAll()
				.anyRequest()
				.authenticated()
			//jwt part, add the filters
				
			//this is for from authentication
			.and()
			.formLogin()//enables form auth
				.usernameParameter("email")//username  field
				.passwordParameter("password")//password field
				.loginPage("/login")//login page, get a nice view
				.defaultSuccessUrl("/profile")//redirect to
			.and()
			.rememberMe()//enables keeping the sessions id as cookie
			.rememberMeParameter("remember-me")//check box field
				.tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))//custom time before toke expires
				.key("customkeyforthis")//defaults to 2 weeks
			.and()
			.logout()//by default logout is enabled, it is for a custom configuration
				.logoutUrl("/logout")//it is a post request
				.clearAuthentication(true)//clear auth
				.invalidateHttpSession(true)//invalidates the session
				.deleteCookies("JSESSIONID","remember-me");//delete cookies
	}
	
	///Auth provider
	@Override
	protected void configure(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(daoAuthenticationProvider());//send the bean
	}
//	it get the user information from the database, applicationUserService has access to db via repository
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider provider= new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder());
		provider.setUserDetailsService(applicationUserService);
		return provider;
	}
	
}
