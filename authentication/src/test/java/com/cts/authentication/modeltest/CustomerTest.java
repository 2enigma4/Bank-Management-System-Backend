package com.cts.authentication.modeltest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.cts.authentication.model.Customer;

public class CustomerTest {

	LocalDate date = LocalDate.now();
	Customer cust = new Customer();
	Customer oldCustomer = (new Customer(1L, "Rajat verma", "Street 344", "Delhi", "India", "rajat@gmail.com",
			"GHDSG56", "9384739233", date, "male", "Savings"));
	Customer newCustomer = (new Customer(1L, "Rajat verma", "Street 344", "Delhi", "India", "rajat@gmail.com",
			"GHDSG56", "9384739233", date, "male", "Savings"));

	@Test
	public void Test_getCustId() {
		cust.setCustId(1);
		assertEquals(1, cust.getCustId());
	}

	@Test
	public void Test_getName() {
		cust.setName("abc");
		assertEquals("abc", cust.getName());
	}

	@Test
	public void Test_getEmail() {
		cust.setEmail("abc@gmail.com");
		assertEquals("abc@gmail.com", cust.getEmail());
	}

	@Test
	public void Test_getAddress() {
		cust.setAddress("address");
		assertEquals("address", cust.getAddress());
	}

	@Test
	public void Test_getState() {
		cust.setState("M.P");
		assertEquals("M.P", cust.getState());
	}

	@Test
	public void Test_getCountry() {
		cust.setCountry("India");
		assertEquals("India", cust.getCountry());
	}

	@Test
	public void Test_getContact() {
		cust.setContact("9999999999");
		assertEquals("9999999999", cust.getContact());
	}

	@Test
	public void Test_getPan() {
		cust.setPan("GEV679");
		assertEquals("GEV679", cust.getPan());
	}

	@Test
	public void Test_getDob() {
		cust.setDob(date);
		assertEquals(date, cust.getDob());
	}

	@Test
	public void Test_getGender() {
		cust.setGender("male");
		assertEquals("male", cust.getGender());

	}

	@Test
	public void Test_getAccountType() {
		cust.setAccountType("Saving");
		assertEquals("Saving", cust.getAccountType());
	}

	@Test
	public void Test_toString() {
		assertEquals(
				"Customer(custId=0, name=null, address=null, state=null, country=null, email=null, pan=null, contact=null, dob=null, gender=null, accountType=null)",
				cust.toString());
	}
}
