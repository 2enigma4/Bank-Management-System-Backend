package com.cts.customer.servicetest;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.cts.customer.exceptions.CustomerNotFoundException;
import com.cts.customer.exceptions.InvalidTokenException;
import com.cts.customer.model.Customer;
import com.cts.customer.model.ValidatedResponse;
import com.cts.customer.repository.CustomerRepository;
import com.cts.customer.restclients.AuthenticationClient;
import com.cts.customer.service.CustomerServiceImpl;


@SpringBootTest
public class CustomerServiceTest {

	@InjectMocks
	private CustomerServiceImpl customerServiceImpl; 
	
	@Mock
	private AuthenticationClient authClient;
	
	@Mock
	private CustomerRepository customerRepo;
	
	LocalDate date = LocalDate.now();
	
	@Test
	public void Test_GetAllCustomers_Success() {
		ValidatedResponse tokenResponse = new ValidatedResponse("aayushi",true);
		ResponseEntity<ValidatedResponse> response = new ResponseEntity<ValidatedResponse>(tokenResponse,HttpStatus.OK);

		List<Customer> dummyList = new ArrayList<Customer>();
		dummyList.add(new Customer(1,"Rajat verma","Street 344","Delhi","India","rajat@gmail.com","GHDSG56","9384739233",date,"male","Savings"));
		
		when(authClient.getValidity("Bearer token")).thenReturn(response);
		when(customerRepo.findAll()).thenReturn(dummyList);
		
		List<Customer> expected = customerServiceImpl.getAllCustomers("Bearer token");
		assertEquals(expected, dummyList);
		
		verify(customerRepo).findAll();
	}
	
	
	@Test
	public void Test_GetAllCustomers_InvalidTokenException2() {
		ValidatedResponse tokenResponse = new ValidatedResponse("aayushi",false);
		ResponseEntity<ValidatedResponse> response = new ResponseEntity<ValidatedResponse>(tokenResponse,HttpStatus.OK);

		List<Customer> dummyList = new ArrayList<Customer>();
		
		when(authClient.getValidity("Bearer token")).thenReturn(response);
		when(customerRepo.findAll()).thenReturn(dummyList);
		
		assertThrows(InvalidTokenException.class,()->customerServiceImpl.getAllCustomers("Bearer token"));
		
		verify(customerRepo, times(0)).findAll();
	}
	
	
	@Test
	public void Test_GetCustomerById_Success() {
		ValidatedResponse tokenResponse = new ValidatedResponse("aayushi",true);
		ResponseEntity<ValidatedResponse> response = new ResponseEntity<ValidatedResponse>(tokenResponse,HttpStatus.OK);
		
		Customer customer = (new Customer(1L,"Rajat verma","Street 344","Delhi","India","rajat@gmail.com","GHDSG56","9384739233",date,"male","Savings"));
	
		when(authClient.getValidity("Bearer token")).thenReturn(response);
		when(customerRepo.findById(customer.getCustId())).thenReturn(Optional.of(customer));
		
		Customer expected = customerServiceImpl.getCustomerById("Bearer token", 1);
		
		assertEquals(expected, customer);
		
		verify(customerRepo, times(1)).findById(1L);
	}
	
	@Test
	public void Test_GetCustomerById_InvalidToken() {
		ValidatedResponse tokenResponse = new ValidatedResponse("aayushi",false);
		ResponseEntity<ValidatedResponse> response = new ResponseEntity<ValidatedResponse>(tokenResponse,HttpStatus.OK);
		
		Customer customer = (new Customer(1L,"Rajat verma","Street 344","Delhi","India","rajat@gmail.com","GHDSG56","9384739233",date,"male","Savings"));
	
		when(authClient.getValidity("Bearer token")).thenReturn(response);
		when(customerRepo.findById(customer.getCustId())).thenReturn(Optional.of(customer));
		
		assertThrows(InvalidTokenException.class,()->customerServiceImpl.getCustomerById("Bearer token", 1));
		verify(customerRepo, times(0)).findById(1L);
	}
	
	@Test
	public void Test_GetCustomerById_CustomerNotFound() {
		ValidatedResponse tokenResponse = new ValidatedResponse("aayushi",true);
		ResponseEntity<ValidatedResponse> response = new ResponseEntity<ValidatedResponse>(tokenResponse,HttpStatus.OK);
		
		Customer customer = (new Customer(1L,"Rajat verma","Street 344","Delhi","India","rajat@gmail.com","GHDSG56","9384739233",date,"male","Savings"));
	
		when(authClient.getValidity("Bearer token")).thenReturn(response);
		when(customerRepo.findById(customer.getCustId())).thenThrow(CustomerNotFoundException.class);
			
		assertThrows(CustomerNotFoundException.class,()->customerServiceImpl.getCustomerById("Bearer token", 1));
		
		verify(customerRepo, times(1)).findById(1L);
	}
	
	
		@Test
	public void Test_SaveCustomer() {
		Customer customer = (new Customer(1L,"Rajat verma","Street 344","Delhi","India","rajat@gmail.com","GHDSG56","9384739233",date,"male","Savings"));
		
		when(customerRepo.save(customer)).thenReturn(customer);
		
		Customer expected = customerServiceImpl.saveCustomer(customer);
		
		assertEquals(expected,customer);
		
		verify(customerRepo, times(1)).save(customer);
	}
	
	@Test
	public void Test_UpdateDetails_Success() {
		ValidatedResponse tokenResponse = new ValidatedResponse("aayushi",true);
		ResponseEntity<ValidatedResponse> response = new ResponseEntity<ValidatedResponse>(tokenResponse,HttpStatus.OK);
		
		Customer oldCustomer = (new Customer(1L,"Rajat verma","Street 344","Delhi","India","rajat@gmail.com","GHDSG56","9384739233",date,"male","Savings"));
		Customer newCustomer = (new Customer(1L,"Rajat verma","Street 344","Maharashtra","India","rajat123@gmail.com","GHDSG56","9384739233",date,"male","Savings"));
		
		when(authClient.getValidity("Bearer token")).thenReturn(response);
		when(customerRepo.findById(oldCustomer.getCustId())).thenReturn(Optional.of(oldCustomer));
		
		Customer expected = customerServiceImpl.updateDetails("Bearer token", 1L, newCustomer);
		
		assertEquals(expected,newCustomer);
		
		verify(customerRepo, times(1)).findById(1L);
		verify(customerRepo, times(1)).save(newCustomer);
	}
	
	@Test
	public void Test_UpdateDetails_InvalidToken() {
		ValidatedResponse tokenResponse = new ValidatedResponse("aayushi",false);
		ResponseEntity<ValidatedResponse> response = new ResponseEntity<ValidatedResponse>(tokenResponse,HttpStatus.OK);
		
		Customer oldCustomer = (new Customer(1L,"Rajat verma","Street 344","Delhi","India","rajat@gmail.com","GHDSG56","9384739233",date,"male","Savings"));
		Customer newCustomer = (new Customer(1L,"Rajat verma","Street 344","Maharashtra","India","rajat123@gmail.com","GHDSG56","9384739233",date,"male","Savings"));
		
		when(authClient.getValidity("Bearer token")).thenReturn(response);
		when(customerRepo.findById(oldCustomer.getCustId())).thenReturn(Optional.of(oldCustomer));
		
		assertThrows(InvalidTokenException.class,()->customerServiceImpl.updateDetails("Bearer token", 1L, newCustomer));
		verify(customerRepo, times(0)).findById(1L);
		verify(customerRepo, times(0)).save(newCustomer);
	}
	
	
}
