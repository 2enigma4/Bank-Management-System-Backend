package com.cts.loan.servicetest;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.cts.loan.exceptions.InvalidTokenException;
import com.cts.loan.model.Loan;
import com.cts.loan.model.ValidatedResponse;
import com.cts.loan.repository.LoanRepository;
import com.cts.loan.restclient.AuthenticationClient;
import com.cts.loan.service.LoanServiceImpl;

@SpringBootTest
public class LoanServiceTest {

	@InjectMocks
	private LoanServiceImpl loanService;
	
	@Mock
	private LoanRepository loanRepo;
	
	@Mock
	private AuthenticationClient authClient;
	
	@Test
	public void test_FindAllLoans_Success() {
		ValidatedResponse tokenResponse = new ValidatedResponse("aayushi",true);
		ResponseEntity<ValidatedResponse> response = new ResponseEntity<ValidatedResponse>(tokenResponse,HttpStatus.OK);

		Loan loan1 = new Loan(1,"Home Loan",8.2);
		Loan loan2 = new Loan(2,"Eduation Loan",5.9);
		Loan loan3 = new Loan(3,"Car Loan",9.4);
		
		List<Loan> loanList = new ArrayList<Loan>();
		
		loanList.add(loan1);
		loanList.add(loan2);
		loanList.add(loan3);

		
		when(authClient.getValidity("Bearer token")).thenReturn(response);
		when(loanRepo.findAll()).thenReturn(loanList);
		
		List<Loan> expected = loanService.findAllLoans("Bearer token");
		
		assertEquals(expected,loanList);
		verify(loanRepo, times(1)).findAll();
	}
	
	@Test
	public void test_FindAllLoans_ThrowsException() {
		ValidatedResponse tokenResponse = new ValidatedResponse("aayushi",false);
		ResponseEntity<ValidatedResponse> response = new ResponseEntity<ValidatedResponse>(tokenResponse,HttpStatus.OK);

		List<Loan> loanList = new ArrayList<Loan>();
		
		when(authClient.getValidity("Bearer token")).thenReturn(response);
		when(loanRepo.findAll()).thenReturn(loanList);
		
		assertThrows(InvalidTokenException.class,()->loanService.findAllLoans("Bearer token"));
	
		verify(loanRepo, times(0)).findAll();
	}
}
