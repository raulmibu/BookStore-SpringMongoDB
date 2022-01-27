package com.cake.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cake.model.User;
import com.cake.repository.IUserRepository;

@Service
public class ApplicationUserService implements UserDetailsService{

	@Autowired
	private  IUserRepository userserrepository;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userserrepository.findByemail(username);
        if (user != null) {
            return new ApplicationUser(
            		user.getEmail(),
            		user.getPassword(),
            		user.getRole().getGrantedAuthorities(),
            		true,
            		true,
            		true, 
            		user.isEnabled(),
            		user);
        } else {
            throw new UsernameNotFoundException(String.format("Username %s not found", username));
        }
		
	}

}
