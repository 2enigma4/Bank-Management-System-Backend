package com.cts.customer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.customer.exceptions.CustomerNotFoundException;
import com.cts.customer.exceptions.InvalidTokenException;
import com.cts.customer.model.Customer;
import com.cts.customer.restclients.AuthenticationClient;
import com.cts.customer.service.CustomerServiceImpl;

@RestController
@RequestMapping("/api")
public class CustomerController {
	
	@Autowired
	private CustomerServiceImpl customerService;
	
	
	@GetMapping("/customers")
	public ResponseEntity<List<Customer>> getCustomersList(@RequestHeader("Authorization") String token) throws InvalidTokenException{
		List<Customer> customerList = customerService.getAllCustomers(token);
		return new ResponseEntity<>(customerList, HttpStatus.OK);
	}
	
	@GetMapping("/customerbyid/{id}")
	public ResponseEntity<Customer> getCustomer(@RequestHeader("Authorization") String token, @PathVariable ("id") long id) throws CustomerNotFoundException,InvalidTokenException{
		Customer customer = customerService.getCustomerById(token,id);
		return new ResponseEntity<>(customer, HttpStatus.OK);
	}

	
	@PostMapping("/savecustomer")
	public ResponseEntity<Customer> insertCustomer(@RequestBody Customer newCustomer){
		
		Customer cust = customerService.saveCustomer(newCustomer);
		return new ResponseEntity<>(cust, HttpStatus.CREATED);
	}
	
	@PutMapping("/updateAccountDetails/{customerId}") 
	public ResponseEntity<Customer> updateAccountDetails(@RequestHeader("Authorization") String token, @RequestBody Customer customer, @PathVariable ("customerId") long id)throws InvalidTokenException{
		Customer updatedDetails = customerService.updateDetails(token, id, customer);
		
		return new ResponseEntity<>(updatedDetails, HttpStatus.OK);
	}
}
