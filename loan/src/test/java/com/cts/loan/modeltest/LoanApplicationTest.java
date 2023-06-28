package com.cts.loan.modeltest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import com.cts.loan.model.LoanApplication;

@SpringBootTest
public class LoanApplicationTest {

	LoanApplication app = new LoanApplication();
	
	@Test
	public void Test_getAppId() {
		app.setApplicationId(201);
		assertEquals(201,app.getApplicationId());
	}
	
	@Test
	public void Test_getCustId() {
		app.setCustomerId(1);
		assertEquals(1,app.getCustomerId());
	}
	
	@Test
	public void Test_getLoanId() {
		app.setLoanId(101);
		assertEquals(101,app.getLoanId());
	}
	
	@Test
	public void Test_getDuration() {
		app.setDuration(2);
		assertEquals(2,app.getDuration());
	}
	
	@Test
	public void Test_getAmount() {
		app.setAmount(2000000);
		assertEquals(2000000,app.getAmount());
	}
	
	@Test
	public void Test_getPaymentMode() {
		app.setPaymentMode("Cash");
		assertEquals("Cash",app.getPaymentMode());
	}
	
	@Test
	public void Test_getDate() {
		LocalDate date = LocalDate.now();
		app.setDate(date);
		assertEquals(date,app.getDate());
	}
	

}
