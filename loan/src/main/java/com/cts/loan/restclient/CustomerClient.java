package com.cts.loan.restclient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.cts.loan.exceptions.CustomerNotFoundException;
import com.cts.loan.exceptions.InvalidTokenException;
import com.cts.loan.model.Customer;


@FeignClient(name = "${customerservice.client.name}"
, url = "${customerservice.client.url}")
public interface CustomerClient {

	@GetMapping("/customers")
	public List<Customer> getCustomersList();
	
	@GetMapping("/customerbyid/{id}")
	public Customer getCustomer(@RequestHeader("Authorization") String token, @PathVariable ("id") long id) throws CustomerNotFoundException,InvalidTokenException;
}
