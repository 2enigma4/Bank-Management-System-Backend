package com.cts.customer.controllertest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.is;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import com.cts.customer.model.Customer;
import com.cts.customer.model.ValidatedResponse;
import com.cts.customer.repository.CustomerRepository;
import com.cts.customer.restclients.AuthenticationClient;
import com.cts.customer.service.CustomerServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@WebMvcTest
public class CustomerControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private CustomerServiceImpl customerService;
	
	@MockBean
	private AuthenticationClient authClient;
	
	@MockBean
	private CustomerRepository customerRepo;
	
	LocalDate date = LocalDate.now();
	
	public static String asJsonString(final Object obj) {
        try {
        	ObjectMapper objectMapper = new ObjectMapper();
        	objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
	
	@Test
	void Test_InsertCustomer() throws Exception {
		Customer customer = (new Customer(1L,"Rajat verma","Street 344","Delhi","India","rajat@gmail.com","GHDSG56","9384739233",date,"male","Savings"));
		when(customerService.saveCustomer(customer)).thenReturn(customer);
		
		RequestBuilder request = MockMvcRequestBuilders.post("/api/savecustomer")
				.accept(MediaType.APPLICATION_JSON)
				.content(asJsonString(customer))
				.contentType(MediaType.APPLICATION_JSON);
				
		
			mockMvc.perform(request)
					.andExpect(status().isCreated())
					.andExpect(jsonPath("$.custId", is(1)))
					.andReturn();
	}
	
	@Test
	void Test_GetCustomer() throws Exception {
		Customer customer = (new Customer(1L,"Rajat verma","Street 344","Delhi","India","rajat@gmail.com","GHDSG56","9384739233",date,"male","Savings"));
		
		ValidatedResponse tokenResponse = new ValidatedResponse("aayushi",true);
		ResponseEntity<ValidatedResponse> response = new ResponseEntity<ValidatedResponse>(tokenResponse,HttpStatus.OK);

		when(authClient.getValidity("Bearer token")).thenReturn(response);
		
		when(customerRepo.findById(1L)).thenReturn(Optional.of(customer));
		
		when(customerService.getCustomerById("Bearer token", 1L)).thenReturn(customer);
		
		RequestBuilder request = MockMvcRequestBuilders.get("/api/customerbyid/1")
		.accept(MediaType.APPLICATION_JSON)
		.header("Authorization", "Bearer token")
		.contentType(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.custId", is(1)))
				.andExpect(jsonPath("$.name", is("Rajat verma")))
				.andReturn();
	}
	
	@Test
	void Test_GetCustomersList() throws Exception {
		Customer customer1 = new Customer(1L,"Rajat verma","Street 344","Delhi","India","rajat@gmail.com","GHDSG56","9384739233",date,"male","Savings");
		Customer customer2 = new Customer(2L,"Rohen Jain","Mayur Vihar Colony","Madhya Pradesh","India","rohan@gmail.com","GH67HJ2","9333896899",date,"male","Savings");
		
		List<Customer> custList = new ArrayList<>();
		custList.add(customer1);
		custList.add(customer2);
		
		ValidatedResponse tokenResponse = new ValidatedResponse("aayushi",true);
		ResponseEntity<ValidatedResponse> response = new ResponseEntity<ValidatedResponse>(tokenResponse,HttpStatus.OK);

		when(authClient.getValidity("Bearer token")).thenReturn(response);
		
		when(customerRepo.findAll()).thenReturn(custList);
		
		when(customerService.getAllCustomers("Bearer token")).thenReturn(custList);
		
		RequestBuilder request = MockMvcRequestBuilders.get("/api/customers")
				.accept(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer token")
				.contentType(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(2)))
				.andReturn();
		}
	
	@Test
	void Test_UpdateAccountDetails() throws Exception {
		Customer oldcustomer = new Customer(1L,"Rajat verma","Street 344","Delhi","India","rajat@gmail.com","GHDSG56","9384739233",date,"male","Savings");
		Customer newcustomer = new Customer(1L,"Rajat verma","Street 344","Maharashtra","India","rajat123@gmail.com","GHDSG56","9384739233",date,"male","Savings");
		
		ValidatedResponse tokenResponse = new ValidatedResponse("aayushi",true);
		ResponseEntity<ValidatedResponse> response = new ResponseEntity<ValidatedResponse>(tokenResponse,HttpStatus.OK);

		when(authClient.getValidity("Bearer token")).thenReturn(response);
		when(customerService.updateDetails("Bearer token", oldcustomer.getCustId(), newcustomer)).thenReturn(newcustomer);
		when(customerService.getCustomerById("Bearer token", oldcustomer.getCustId())).thenReturn(oldcustomer);
		
		RequestBuilder request = MockMvcRequestBuilders.put("/api/updateAccountDetails/1")
				.accept(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer token")
				.content(asJsonString(newcustomer))
				.contentType(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.state", is("Maharashtra")))
				.andExpect(jsonPath("$.email", is("rajat123@gmail.com")))
				.andExpect(jsonPath("$.contact", is("9384739233")))
				.andReturn();
	}
		
	
}
