package com.cts.customer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.customer.exceptions.CustomerNotFoundException;
import com.cts.customer.exceptions.InvalidTokenException;
import com.cts.customer.model.Customer;
import com.cts.customer.model.ValidatedResponse;
import com.cts.customer.repository.CustomerRepository;
import com.cts.customer.restclients.AuthenticationClient;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository repo;

	@Autowired
	private AuthenticationClient authClient;

	public List<Customer> getAllCustomers(String token) throws InvalidTokenException {
		ValidatedResponse body = authClient.getValidity(token).getBody();
		if (body!=null && body.isValid()) {
			 return repo.findAll();
		} else {
			throw new InvalidTokenException();
		}
		
	}
	
	public Customer getCustomerById(String token, long id) throws CustomerNotFoundException, InvalidTokenException {
		ValidatedResponse body = authClient.getValidity(token).getBody();
		if (body!=null && body.isValid()) {			
			return repo.findById(id).orElseThrow(()-> new CustomerNotFoundException());
		} else {
			throw new InvalidTokenException();
		}

	}

	public Customer saveCustomer(Customer customer) {
		return repo.save(customer);
	}

	
	public Customer updateDetails(String token, long custId, Customer cust)throws InvalidTokenException{
		
		ValidatedResponse body = authClient.getValidity(token).getBody();
		if (body!=null && body.isValid()) {
			Customer updatedCustomer = getCustomerById(token, custId);
			
			updatedCustomer.setName(cust.getName());
			updatedCustomer.setAddress(cust.getAddress());
			updatedCustomer.setContact(cust.getContact());
			updatedCustomer.setCountry(cust.getCountry());
			updatedCustomer.setDob(cust.getDob());
			updatedCustomer.setEmail(cust.getEmail());
			updatedCustomer.setGender(cust.getGender());
			updatedCustomer.setPan(cust.getPan());
			updatedCustomer.setState(cust.getState());
			updatedCustomer.setAccountType(cust.getAccountType());
			
			repo.save(updatedCustomer);
			
			return updatedCustomer;
			
		}
		else {
			throw new InvalidTokenException();
		}
		
		
	}
}
