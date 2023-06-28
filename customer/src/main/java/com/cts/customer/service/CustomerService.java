package com.cts.customer.service;

import java.util.List;

import com.cts.customer.exceptions.CustomerNotFoundException;
import com.cts.customer.exceptions.InvalidTokenException;
import com.cts.customer.model.Customer;

public interface CustomerService {
	
	public Customer getCustomerById(String token, long id)throws CustomerNotFoundException,InvalidTokenException;
	
	public List<Customer> getAllCustomers(String token) throws InvalidTokenException;
	
	public Customer saveCustomer(Customer customer);
	
	public Customer updateDetails(String token, long custId, Customer cust)throws InvalidTokenException;
	
}
