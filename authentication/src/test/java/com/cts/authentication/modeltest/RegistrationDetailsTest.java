package com.cts.authentication.modeltest;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.cts.authentication.model.Customer;
import com.cts.authentication.model.LoginCredentials;
import com.cts.authentication.model.RegistrationDetails;

@SpringBootTest
public class RegistrationDetailsTest {

	LocalDate date = LocalDate.now();
	RegistrationDetails details = new RegistrationDetails();
	LoginCredentials login = new LoginCredentials(1,"Aayushi","pass123");
	Customer cust = new Customer(1,"Rajat Verma","Street 344","Delhi","India","rajat@gmail.com","GHDSG56","9384739233",date,"male","Savings");
	
	@Test
	public void Test_getCustomer() {
		details.setCustomer(cust);
		assertEquals("Rajat Verma",details.getCustomer().getName());
	}
	
	@Test
	public void Test_getCredentials() {
		details.setLoginCredentials(login);
		assertEquals("Aayushi",details.getLoginCredentials().getUsername());
	}
}
