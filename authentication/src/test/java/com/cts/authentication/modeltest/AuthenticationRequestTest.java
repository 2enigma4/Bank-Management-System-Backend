package com.cts.authentication.modeltest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.cts.authentication.model.AuthenticationRequest;

@SpringBootTest
public class AuthenticationRequestTest {
	
	AuthenticationRequest request = new AuthenticationRequest();
	
	@Test
	public void Test_GetUsername() {
		request.setUsername("Aayushi");
		assertEquals("Aayushi",request.getUsername());
	}
	
	@Test
	public void Test_GetPassword() {
		request.setPassword("pass");
		assertEquals("pass",request.getPassword());
	}
}
