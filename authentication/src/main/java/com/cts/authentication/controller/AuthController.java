package com.cts.authentication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.authentication.exceptions.InvalidCredentialsException;
import com.cts.authentication.exceptions.InvalidTokenException;
import com.cts.authentication.exceptions.UsernameAlreadyExistsException;
import com.cts.authentication.model.AuthenticationRequest;
import com.cts.authentication.model.AuthenticationResponse;
import com.cts.authentication.model.LoginCredentials;
import com.cts.authentication.model.RegistrationDetails;
import com.cts.authentication.model.ValidatedResponse;
import com.cts.authentication.restclients.CustomerClient;
import com.cts.authentication.service.MyUserDetailsService;
import com.cts.authentication.util.JwtUtil;

@RestController
@RequestMapping("/api")
public class AuthController {
	
	@Autowired
	private MyUserDetailsService myUserDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private CustomerClient custClient;
	

	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest ) throws InvalidCredentialsException {
		try{
				authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
				);
		}
		catch(Exception e) {
			throw new InvalidCredentialsException();
		}
		
		final UserDetails userDetails = myUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		
		final String jwt = jwtUtil.generateToken(userDetails);
		
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
	
	
	
	@GetMapping(value = "/validate")
	public ResponseEntity<ValidatedResponse> getValidity(@RequestHeader("Authorization") final String requestToken) throws InvalidTokenException {
		
		String jwtToken = null;
		ValidatedResponse res = new ValidatedResponse();
		
		if(requestToken!=null && requestToken.startsWith("Bearer ")) {
			jwtToken = requestToken.substring(7);
			Boolean vald = jwtUtil.validateToken(jwtToken);
			res.setUsername(jwtUtil.extractUsername(jwtToken));
			if(Boolean.TRUE.equals(vald)) {
				res.setValid(true);
			}
		}
		else {
			throw new InvalidTokenException();
		}
		
		return new ResponseEntity<>(res, HttpStatus.OK);
		
	}
	
	
	@PostMapping("/register")
	public ResponseEntity<LoginCredentials> register(@RequestBody RegistrationDetails details) throws UsernameAlreadyExistsException {
		if(!myUserDetailsService.customerExistsWithUsername(details.getLoginCredentials())){
			LoginCredentials newUser = myUserDetailsService.saveCredentials(details.getLoginCredentials());
			long customerId = newUser.getId();
			details.getCustomer().setCustId(customerId);
			custClient.insertCustomer(details.getCustomer());
			
			return new ResponseEntity<>(newUser, HttpStatus.CREATED);

		}
		else {
			throw new UsernameAlreadyExistsException();
		}
	}
	

}
