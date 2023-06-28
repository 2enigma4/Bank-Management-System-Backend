package com.cts.loan.servicetest;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.Assert.assertThrows;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.cts.loan.exceptions.CustomersAbove5LNotFoundException;
import com.cts.loan.exceptions.InvalidTokenException;
import com.cts.loan.exceptions.LoanApplicationsNotFoundException;
import com.cts.loan.model.Customer;
import com.cts.loan.model.LoanApplication;
import com.cts.loan.model.ValidatedResponse;
import com.cts.loan.repository.LoanApplicationRepository;
import com.cts.loan.restclient.AuthenticationClient;
import com.cts.loan.restclient.CustomerClient;
import com.cts.loan.service.LoanApplicationServiceImpl;

@SpringBootTest
public class LoanApplicationServiceTest {

	 @InjectMocks
	 private LoanApplicationServiceImpl loanApplicationService;
	
	 @Mock
	 private LoanApplicationRepository loanApplicationRepo;
	 
	 @Mock
	 private CustomerClient customerClient;
	 
	 @Mock
	 private AuthenticationClient authClient;
	 
	 LocalDate date = LocalDate.of(2021,9,12);
	 
	 @Test
	 public void Test_Save_NewApplication_Success() {
		ValidatedResponse tokenResponse = new ValidatedResponse("aayushi",true);
		ResponseEntity<ValidatedResponse> response = new ResponseEntity<ValidatedResponse>(tokenResponse,HttpStatus.OK);
		
		LoanApplication app = new LoanApplication(101,1,201,500000,2,"Cash",date);
		
		when(authClient.getValidity("Bearer token")).thenReturn(response);
		when(loanApplicationRepo.save(app)).thenReturn(app);
		
		LoanApplication expected = loanApplicationService.saveNewApplication("Bearer token", app);
		
		assertEquals(expected, app);
		verify(loanApplicationRepo, times(1)).save(app);
		
	 }
	 
	 @Test
	 public void Test_Save_NewApplication_Throws_InvalidTokenException() {
		ValidatedResponse tokenResponse = new ValidatedResponse("aayushi",false);
		ResponseEntity<ValidatedResponse> response = new ResponseEntity<ValidatedResponse>(tokenResponse,HttpStatus.OK);
		
		LoanApplication app = new LoanApplication(101,1,201,500000,2,"Cash",date);
		
		when(authClient.getValidity("Bearer token")).thenReturn(response);
		when(loanApplicationRepo.save(app)).thenReturn(app);
		
		assertThrows(InvalidTokenException.class, ()->loanApplicationService.saveNewApplication("Bearer token", app));
		verify(loanApplicationRepo, times(0)).save(app);
		
	 }
	 
	 @Test
	 public void Test_Find_AllLoanApplication() {
		
		List<LoanApplication> loanList = new ArrayList<>();
		LoanApplication app = new LoanApplication(101,1,201,500000,2,"Cash",date);
		loanList.add(app);
		
		when(loanApplicationRepo.findAll()).thenReturn(loanList);
		
		List<LoanApplication> expected = loanApplicationService.findAllLoanApplications();
		
		assertEquals(expected,loanList );
		verify(loanApplicationRepo, times(1)).findAll();
		
	 }
	 
	 @Test
	 public void Test_FindLoanApplications_ByLoanId_Success() {
		ValidatedResponse tokenResponse = new ValidatedResponse("aayushi",true);
		ResponseEntity<ValidatedResponse> response = new ResponseEntity<ValidatedResponse>(tokenResponse,HttpStatus.OK);
		
		List<LoanApplication> loanList = new ArrayList<>();
		LoanApplication app = new LoanApplication(101,1,201,500000,2,"Cash",date);
		loanList.add(app);
		
		when(authClient.getValidity("Bearer token")).thenReturn(response);
		when(loanApplicationRepo.getLoanApplicationByLoanId(201)).thenReturn(loanList);
		
		List<LoanApplication> expected = loanApplicationService.findLoanApplicationsByLoanId("Bearer token", 201);
		
		assertEquals(expected,loanList );
		assertEquals(201,loanList.get(0).getLoanId());
		verify(loanApplicationRepo, times(1)).getLoanApplicationByLoanId(201);
	 }
	 
	 @Test
	 public void Test_FindLoanApplications_ByLoanId_Throws_LoanApplicationNotFoundException() {
		ValidatedResponse tokenResponse = new ValidatedResponse("aayushi",true);
		ResponseEntity<ValidatedResponse> response = new ResponseEntity<ValidatedResponse>(tokenResponse,HttpStatus.OK);
		
		List<LoanApplication> loanList = new ArrayList<>();
		
		when(authClient.getValidity("Bearer token")).thenReturn(response);
		when(loanApplicationRepo.getLoanApplicationByLoanId(201)).thenReturn(loanList);
		
		assertThrows(LoanApplicationsNotFoundException.class,()->loanApplicationService.findLoanApplicationsByLoanId("Bearer token", 201));
		verify(loanApplicationRepo, times(1)).getLoanApplicationByLoanId(201);
	 }
	 
	 @Test
	 public void Test_FindLoanApplications_ByLoanId_Throws_InvalidTokenException() {
		ValidatedResponse tokenResponse = new ValidatedResponse("aayushi",false);
		ResponseEntity<ValidatedResponse> response = new ResponseEntity<ValidatedResponse>(tokenResponse,HttpStatus.OK);
		
		List<LoanApplication> loanList = new ArrayList<>();
		LoanApplication app = new LoanApplication(101,1,201,500000,2,"Cash",date);
		loanList.add(app);
		
		when(authClient.getValidity("Bearer token")).thenReturn(response);
		when(loanApplicationRepo.getLoanApplicationByLoanId(201)).thenReturn(loanList);
		
		assertThrows(InvalidTokenException.class,()->loanApplicationService.findLoanApplicationsByLoanId("Bearer token", 201));
		verify(loanApplicationRepo, times(0)).getLoanApplicationByLoanId(201);
	 }
	 
	 @Test
	 public void Test_FindLoanApplications_ByLoanAmount_Success() {
		ValidatedResponse tokenResponse = new ValidatedResponse("aayushi",true);
		ResponseEntity<ValidatedResponse> response = new ResponseEntity<ValidatedResponse>(tokenResponse,HttpStatus.OK);
		
		List<LoanApplication> loanList = new ArrayList<>();
		LoanApplication app1 = new LoanApplication(101,1,201,900000,2,"Cash",date);
		LoanApplication app2 = new LoanApplication(102,2,202,400000,2,"Cash",date);
		LoanApplication app3 = new LoanApplication(103,3,201,700000,2,"Cash",date);
		
		loanList.add(app1);
		loanList.add(app2);
		loanList.add(app3);
		
		when(authClient.getValidity("Bearer token")).thenReturn(response);
		when(loanApplicationService.findAllLoanApplications()).thenReturn(loanList);
		
		Customer customer1 = new Customer(1,"Rajat verma","Street 344","Delhi","India","rajat@gmail.com","GHDSG56","9384739233",date,"male","Savings");
		Customer customer2 = new Customer(3,"Rohen Jain","Mayur Vihar Colony","Madhya Pradesh","India","rohan@gmail.com","GH67HJ2","9333896899",date,"male","Savings");
		
		List<Customer> custList = new ArrayList<>();
		custList.add(customer1);
		custList.add(customer2);

		doReturn(customer1).when(customerClient).getCustomer("Bearer token", 1);
		doReturn(customer2).when(customerClient).getCustomer("Bearer token", 3);

		List<Customer> expected = loanApplicationService.findLoanApplicationsByLoanAmount("Bearer token");

		assertEquals(expected, custList);
	}
	 
	 @Test
	 public void Test_FindLoanApplications_ByLoanAmount_InvalidTokenException() {
		ValidatedResponse tokenResponse = new ValidatedResponse("aayushi",false);
		ResponseEntity<ValidatedResponse> response = new ResponseEntity<ValidatedResponse>(tokenResponse,HttpStatus.OK);
		
		List<LoanApplication> loanList = new ArrayList<>();
		LoanApplication app1 = new LoanApplication(101,1,201,900000,2,"Cash",date);
		LoanApplication app2 = new LoanApplication(102,2,202,400000,2,"Cash",date);
		LoanApplication app3 = new LoanApplication(103,3,201,700000,2,"Cash",date);
		
		loanList.add(app1);
		loanList.add(app2);
		loanList.add(app3);
		
		when(authClient.getValidity("Bearer token")).thenReturn(response);
		when(loanApplicationService.findAllLoanApplications()).thenReturn(loanList);
		
		Customer customer1 = new Customer(1,"Rajat verma","Street 344","Delhi","India","rajat@gmail.com","GHDSG56","9384739233",date,"male","Savings");
		Customer customer2 = new Customer(3,"Rohen Jain","Mayur Vihar Colony","Madhya Pradesh","India","rohan@gmail.com","GH67HJ2","9333896899",date,"male","Savings");
		
		List<Customer> custList = new ArrayList<>();
		custList.add(customer1);
		custList.add(customer2);

		doReturn(customer1).when(customerClient).getCustomer("Bearer token", 1);
		doReturn(customer2).when(customerClient).getCustomer("Bearer token", 3);

		assertThrows(InvalidTokenException.class, ()->loanApplicationService.findLoanApplicationsByLoanAmount("Bearer token"));
	}
	 
	 @Test
	 public void Test_FindLoanApplications_ByLoanAmount_CustomersAbove5LNotFoundException() {
		ValidatedResponse tokenResponse = new ValidatedResponse("aayushi",true);
		ResponseEntity<ValidatedResponse> response = new ResponseEntity<ValidatedResponse>(tokenResponse,HttpStatus.OK);
		
		List<LoanApplication> loanList = new ArrayList<>();
		LoanApplication app1 = new LoanApplication(101,1,201,300000,2,"Cash",date);
		LoanApplication app2 = new LoanApplication(102,2,202,400000,2,"Cash",date);
		LoanApplication app3 = new LoanApplication(103,3,201,200000,2,"Cash",date);
		
		loanList.add(app1);
		loanList.add(app2);
		loanList.add(app3);
		
		when(authClient.getValidity("Bearer token")).thenReturn(response);
		when(loanApplicationService.findAllLoanApplications()).thenReturn(loanList);
		
		assertThrows(CustomersAbove5LNotFoundException.class, ()->loanApplicationService.findLoanApplicationsByLoanAmount("Bearer token"));
	}
	 
}
