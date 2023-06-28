package com.cts.authentication.restclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.cts.authentication.model.Customer;

@FeignClient(name = "${customerservice.client.name}"
, url = "${customerservice.client.url}")
public interface CustomerClient {

	@PostMapping("/savecustomer")
	public Customer insertCustomer(@RequestBody Customer newCustomer);
}
