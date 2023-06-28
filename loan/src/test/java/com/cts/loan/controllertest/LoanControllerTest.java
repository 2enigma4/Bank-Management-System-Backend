package com.cts.loan.controllertest;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cts.loan.exceptions.LoanApplicationsNotFoundException;
import com.cts.loan.model.Customer;
import com.cts.loan.model.Loan;
import com.cts.loan.model.LoanApplication;
import com.cts.loan.model.ValidatedResponse;
import com.cts.loan.repository.LoanApplicationRepository;
import com.cts.loan.repository.LoanRepository;
import com.cts.loan.restclient.AuthenticationClient;
import com.cts.loan.restclient.CustomerClient;
import com.cts.loan.service.LoanApplicationServiceImpl;
import com.cts.loan.service.LoanServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@WebMvcTest
public class LoanControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private AuthenticationClient authClient;
	
	@MockBean
	private CustomerClient customerClient;
	
	@MockBean
	private LoanServiceImpl loanService;
	
	@MockBean
	private LoanApplicationServiceImpl loanApplicationService;
	
	@MockBean
	private LoanRepository loanRepo;
	
	@MockBean
	private LoanApplicationRepository loanApplicationRepo;
	
	LocalDate date = LocalDate.of(1992, 04, 24);
	
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
	public void test_GetAllLoans() throws Exception {
		Loan loan1 = new Loan(1,"Home Loan",8.2);
		Loan loan2 = new Loan(2,"Eduation Loan",5.9);
		Loan loan3 = new Loan(3,"Car Loan",9.4);
		
		List<Loan> loanList = new ArrayList<Loan>();
		
		loanList.add(loan1);
		loanList.add(loan2);
		loanList.add(loan3);
		
		ValidatedResponse tokenResponse = new ValidatedResponse("aayushi",true);
		ResponseEntity<ValidatedResponse> response = new ResponseEntity<ValidatedResponse>(tokenResponse,HttpStatus.OK);

		when(authClient.getValidity("Bearer token")).thenReturn(response);
		
		when(loanRepo.findAll()).thenReturn(loanList);
		
		when(loanService.findAllLoans("Bearer token")).thenReturn(loanList);
		
		
		
		RequestBuilder request = MockMvcRequestBuilders.get("/api/loans")
				.accept(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer token")
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(3)))
				.andExpect(content().json("[{\"loanId\":1,\"loanType\":\"Home Loan\",\"interestRate\":8.2},{\"loanId\":2,\"loanType\":\"Eduation Loan\",\"interestRate\":5.9},{\"loanId\":3,\"loanType\":\"Car Loan\",\"interestRate\":9.4}]"))
				.andReturn();
		
	}
	
	@Test
	public void test_GetLoanApplications_ByLoanId() throws Exception {
		LoanApplication app = new LoanApplication(101,1,201,500000,2,"Cash",date);
		List<LoanApplication> appList = new ArrayList<>();
		appList.add(app);
		
		ValidatedResponse tokenResponse = new ValidatedResponse("aayushi",true);
		ResponseEntity<ValidatedResponse> response = new ResponseEntity<ValidatedResponse>(tokenResponse,HttpStatus.OK);
		
		when(authClient.getValidity("Bearer token")).thenReturn(response);
		when(loanApplicationService.findLoanApplicationsByLoanId("Bearer token", 201)).thenReturn(appList);
		when(loanApplicationRepo.getLoanApplicationByLoanId(201)).thenReturn(appList);
		
		RequestBuilder request = MockMvcRequestBuilders.get("/api/loanapplications/201")
				.accept(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer token")
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(1)))
				.andExpect(content().json("[{\"applicationId\":101,\"customerId\":1,\"loanId\":201,\"amount\":500000.0,\"duration\":2,\"paymentMode\":\"Cash\",\"date\":\"1992-04-24\"}]"))
				.andReturn();
		
		
	}
	
	@Test
	public void test_GetLoanApplications_ByLoanId_Throws_NoApplicationFound() throws Exception {
		ValidatedResponse tokenResponse = new ValidatedResponse("aayushi",true);
		ResponseEntity<ValidatedResponse> response = new ResponseEntity<ValidatedResponse>(tokenResponse,HttpStatus.OK);
		
		when(authClient.getValidity("Bearer token")).thenReturn(response);
		when(loanApplicationService.findLoanApplicationsByLoanId("Bearer token", 202)).thenThrow(LoanApplicationsNotFoundException.class);
		when(loanApplicationRepo.getLoanApplicationByLoanId(202)).thenThrow(LoanApplicationsNotFoundException.class);
		
		RequestBuilder request = MockMvcRequestBuilders.get("/api/loanapplications/202")
				.accept(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer token")
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(request)
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message",is("Loan Applications Not Found")))
				.andReturn();
	}
	
	@Test
	public void test_getAllLoanApplications_ByAmount() throws Exception {
		LoanApplication app = new LoanApplication(101,1,201,600000,2,"Cash",date);
		
		Customer customer = new Customer(1L,"Rajat verma","Street 344","Delhi","India","rajat@gmail.com","GHDSG56","9384739233",date,"male","Savings");
		
		List<LoanApplication> appList = new ArrayList<>();
		appList.add(app);
		
		List<Customer> custList = new ArrayList<>();
		custList.add(customer);
		
		ValidatedResponse tokenResponse = new ValidatedResponse("aayushi",true);
		ResponseEntity<ValidatedResponse> response = new ResponseEntity<ValidatedResponse>(tokenResponse,HttpStatus.OK);
		
		when(authClient.getValidity("Bearer token")).thenReturn(response);
		when(loanApplicationService.findLoanApplicationsByLoanAmount("Bearer token")).thenReturn(custList);
		when(customerClient.getCustomer("Bearer token",1)).thenReturn(customer);
		when(loanApplicationRepo.findAll()).thenReturn(appList);
		
		RequestBuilder request = MockMvcRequestBuilders.get("/api/loanabove5")
				.accept(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer token")
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(1)))
				.andExpect(content().json("[{\"custId\":1,\"name\":\"Rajat verma\",\"address\":\"Street 344\",\"state\":\"Delhi\",\"country\":\"India\",\"email\":\"rajat@gmail.com\",\"pan\":\"GHDSG56\",\"contact\":\"9384739233\",\"dob\":\"1992-04-24\",\"gender\":\"male\",\"accountType\":\"Savings\"}]"))
				.andReturn();
		
	}
	
//	@Test
//	public void test_ApplyForLoan_Success() throws Exception {
//		LoanApplication app = new LoanApplication(101,1,201,600000,2,"Cash",date);
//		
//		ValidatedResponse tokenResponse = new ValidatedResponse("aayushi",true);
//		ResponseEntity<ValidatedResponse> response = new ResponseEntity<ValidatedResponse>(tokenResponse,HttpStatus.OK);
//		
//		when(authClient.getValidity("Bearer token")).thenReturn(response);
//		when(loanApplicationService.saveNewApplication("Bearer token", app)).thenReturn(app);
//		when(loanApplicationRepo.save(app)).thenReturn(app);
//		
//		RequestBuilder request = MockMvcRequestBuilders.post("/api/applyloan")
//				.accept(MediaType.APPLICATION_JSON)
//				.header("Authorization", "Bearer token")
//				.content(asJsonString(app))
//				.contentType(MediaType.APPLICATION_JSON);
//		
//		MvcResult result = mockMvc.perform(request)
//				.andExpect(status().isCreated())
//				.andExpect(content().json("{\"applicationId\":101,\"customerId\":1,\"loanId\":201,\"amount\":600000.0,\"duration\":2,\"paymentMode\":\"Cash\",\"date\":\"1992-04-24\"}"))
//				.andReturn();
//		
//		System.out.println(result.getResponse().getContentAsString());
//		
//	}
	
	
	
	
}
