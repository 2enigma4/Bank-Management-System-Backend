package com.cts.authentication.controllertest;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cts.authentication.exceptions.InvalidCredentialsException;
import com.cts.authentication.exceptions.InvalidTokenException;
import com.cts.authentication.model.AuthenticationRequest;
import com.cts.authentication.repository.AuthRepository;
import com.cts.authentication.restclients.CustomerClient;
import com.cts.authentication.service.MyUserDetailsService;
import com.cts.authentication.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@WebMvcTest
public class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private MyUserDetailsService myUserDetailsService;
	
	@MockBean
	private JwtUtil jwtUtil;
	
	@MockBean
	private AuthenticationManager authenticationManager;
	
	@MockBean
	private CustomerClient custClient;
	
	@MockBean
	private AuthRepository authRepository;
	
	
	
	LocalDate date = LocalDate.of(2022, 9, 23);
	
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
	public void Test_CreateAuthenticationToken_Success() throws Exception {
		AuthenticationRequest request = new AuthenticationRequest("Aayushi","pass");
		UserDetails user = new User(request.getUsername(),request.getPassword(),new ArrayList<>());
		
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword());
		
		when(authenticationManager.authenticate(auth)).thenReturn(auth);
		
		when(myUserDetailsService.loadUserByUsername(request.getUsername())).thenReturn(user);
		
		when(jwtUtil.generateToken(user)).thenReturn("token");
		
		RequestBuilder httprequest = MockMvcRequestBuilders.post("/api/login")
				.accept(MediaType.APPLICATION_JSON)
				.content(asJsonString(request))
				.contentType(MediaType.APPLICATION_JSON);
		
		 mockMvc.perform(httprequest)
				.andExpect(status().isOk())
				.andExpect(content().json("{\"jwt\":\"token\"}"))
				.andReturn();
	}
	
	@Test
	public void Test_CreateAuthenticationToken_ThrowsException() throws Exception {
		AuthenticationRequest request = new AuthenticationRequest("Username","pass");
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword());
		
		when(authenticationManager.authenticate(auth)).thenThrow(InvalidCredentialsException.class);
		
		RequestBuilder httprequest = MockMvcRequestBuilders.post("/api/login")
				.accept(MediaType.APPLICATION_JSON)
				.content(asJsonString(request))
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(httprequest)
				.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidCredentialsException))
				.andReturn();
	}
	
	@Test
	public void Test_GetValidity_Success() throws Exception {
		when(jwtUtil.validateToken("token")).thenReturn(true);
		when(jwtUtil.extractUsername("token")).thenReturn("Aayushi");
		
		RequestBuilder httprequest = MockMvcRequestBuilders.get("/api/validate")
				.accept(MediaType.APPLICATION_JSON)
				.header("Authorization","Bearer token")
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(httprequest)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("Aayushi"))
				.andExpect(jsonPath("$.valid").value(true))
				.andReturn();
	}
	
	@Test
	public void Test_GetValidity_Fail() throws Exception {
		when(jwtUtil.validateToken("token")).thenReturn(false);
		when(jwtUtil.extractUsername("token")).thenReturn("Aayushi");
		
		RequestBuilder httprequest = MockMvcRequestBuilders.get("/api/validate")
				.accept(MediaType.APPLICATION_JSON)
				.header("Authorization","Bearer token")
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(httprequest)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("Aayushi"))
				.andExpect(jsonPath("$.valid").value(false))
				.andReturn();
	}
	
	@Test
	public void Test_GetValidity_ThrowsException1() throws Exception {
		RequestBuilder httprequest = MockMvcRequestBuilders.get("/api/validate")
				.accept(MediaType.APPLICATION_JSON)
				.header("Authorization",Optional.ofNullable(null)) //token is null
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(httprequest)
				.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidTokenException))
				.andReturn();
	
	}
	
	@Test
	public void Test_GetValidity_ThrowsException2() throws Exception {
		RequestBuilder httprequest = MockMvcRequestBuilders.get("/api/validate")
				.accept(MediaType.APPLICATION_JSON)
				.header("Authorization","token") //Bearer not given
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(httprequest)
				.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidTokenException))
				.andReturn();
	
	}
	
//	@Test
//	public void Test_Register() throws Exception{
//		LoginCredentials login = new LoginCredentials(1,"Aayushi","pass123");
//		Customer cust = new Customer(1,"Rajat verma","Street 344","Delhi","India","rajat@gmail.com","GHDSG56","9384739233",date,"male","Savings");
//		
//		RegistrationDetails details = new RegistrationDetails(cust,login);
//		
//		when(myUserDetailsService.customerExistsWithUsername(login)).thenReturn(false);
//		
//		when(authRepository.save(login)).thenReturn(login);
//		
//		when(myUserDetailsService.saveCredentials(login)).thenReturn(login);
//		
//		when(custClient.insertCustomer(cust)).thenReturn(cust);
//		
//		RequestBuilder httprequest = MockMvcRequestBuilders.post("/api/register")
//				.accept(MediaType.APPLICATION_JSON)
//				.content(asJsonString(details))
//				.contentType(MediaType.APPLICATION_JSON);
//		
//		MvcResult result = mockMvc.perform(httprequest)
//				.andExpect(status().isCreated())
//				.andExpect(jsonPath("$.username").value("Aayushi"))
//				.andExpect(jsonPath("$.id").value(1))
//				.andReturn();
//		
////		System.out.println(result.getResponse().getContentAsString());
//
//	}

//	@Test
//	public void Test_Register_Failure() throws Exception{
//		LoginCredentials login = new LoginCredentials(1,"Aayushi","pass123");
//		Customer cust = new Customer(1,"Rajat verma","Street 344","Delhi","India","rajat@gmail.com","GHDSG56","9384739233",date,"male","Savings");
//		
//		RegistrationDetails details = new RegistrationDetails(cust,login);
//		
//		when(myUserDetailsService.customerExistsWithUsername(login)).thenReturn(true);
//		when(authRepository.findByUserName(login.getUsername())).thenReturn(login);
//		
//		RequestBuilder httprequest = MockMvcRequestBuilders.post("/api/register")
//				.accept(MediaType.APPLICATION_JSON)
//				.content(asJsonString(details))
//				.contentType(MediaType.APPLICATION_JSON);
//		
//			mockMvc.perform(httprequest)
//				.andExpect(status().isForbidden())
//				.andExpect(result -> assertTrue(result.getResolvedException() instanceof UsernameAlreadyExistsException))
//				.andReturn();
//
//	}
}
