package com.cts.authentication.service;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cts.authentication.model.LoginCredentials;
import com.cts.authentication.repository.AuthRepository;

@Service
public class MyUserDetailsService implements UserDetailsService{

	@Autowired
	private AuthRepository authRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		LoginCredentials myUser = authRepository.findByUserName(username);
		if(myUser!=null) {
			return new User(myUser.getUsername(),myUser.getPassword(),new ArrayList<>());
		}
		else{
			throw new UsernameNotFoundException("Username not found");
		}
		
	}
	
	public LoginCredentials saveCredentials(LoginCredentials myUser) {
		 return authRepository.save(myUser);
	}
	
	public boolean customerExistsWithUsername(LoginCredentials credentials) {
		boolean flag = false;
		LoginCredentials myUser = authRepository.findByUserName(credentials.getUsername());
		if(myUser != null) {
			flag=true;
		}
		return flag;
	}

}
