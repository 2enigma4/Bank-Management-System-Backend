package com.cts.loan.modeltest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.cts.loan.model.Loan;

@SpringBootTest
public class LoanTest {

	Loan loan = new Loan();
	
	@Test
	public void Test_getLoanId() {
		loan.setLoanId(101);
		assertEquals(101,loan.getLoanId());
	}
	
	@Test
	public void Test_getLoanType() {
		loan.setLoanType("Home");
		assertEquals("Home",loan.getLoanType());
	}
	
	@Test
	public void Test_getInterestRate() {
		loan.setInterestRate(4.7);
		assertEquals(4.7,loan.getInterestRate());
	}
	
	
}
