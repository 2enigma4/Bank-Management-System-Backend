package com.cts.authentication.modeltest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.cts.authentication.model.LoginCredentials;

@SpringBootTest
public class LoginCredentialsTest {

	LoginCredentials login2 =  new LoginCredentials();
	
	@Test
	public void Test_Id() {
		login2.setId(1);
		assertEquals(1,login2.getId());
	}
	
	@Test
	public void Test_Username() {
		login2.setUsername("Aayushi");;
		assertEquals("Aayushi",login2.getUsername());
	}
	
	@Test
	public void Test_Password() {
		login2.setPassword("pass");;
		assertEquals("pass",login2.getPassword());
	}
	
}
