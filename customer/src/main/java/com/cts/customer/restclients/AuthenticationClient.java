package com.cts.customer.restclients;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestHeader;

import com.cts.customer.exceptions.InvalidTokenException;
import com.cts.customer.model.ValidatedResponse;



@FeignClient(name="${authenticationservice.client.name}"
	,url="${authenticationservice.client.url}")
public interface AuthenticationClient {
	

	@GetMapping(value = "/validate")
	public ResponseEntity<ValidatedResponse> getValidity(@RequestHeader("Authorization") final String requestToken) throws InvalidTokenException ;
		
}
