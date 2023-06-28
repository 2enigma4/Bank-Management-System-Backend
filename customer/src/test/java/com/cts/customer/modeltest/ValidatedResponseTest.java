package com.cts.customer.modeltest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.cts.customer.model.ValidatedResponse;

@SpringBootTest
public class ValidatedResponseTest {

	ValidatedResponse response = new ValidatedResponse();
	
	@Test
	public void Test_Username() {
		response.setUsername("Aayushi");
		assertEquals("Aayushi",response.getUsername());
	}
	
	@Test
	public void Test_IsValid() {
		response.setValid(true);
		assertTrue(response.isValid());
	}
	
	@Test
	public void Test_toString() {
		assertEquals("ValidatedResponse(username=null, isValid=false)",response.toString());
	}
}
