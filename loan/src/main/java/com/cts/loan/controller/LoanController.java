package com.cts.loan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.loan.model.LoanApplication;
import com.cts.loan.exceptions.CustomersAbove5LNotFoundException;
import com.cts.loan.exceptions.InvalidTokenException;
import com.cts.loan.exceptions.LoanApplicationsNotFoundException;
import com.cts.loan.model.Customer;
import com.cts.loan.model.Loan;
import com.cts.loan.service.LoanApplicationServiceImpl;
import com.cts.loan.service.LoanServiceImpl;


@RestController
@RequestMapping("/api")
public class LoanController {
	
	@Autowired
	private LoanServiceImpl loanService;
	
	@Autowired
	private LoanApplicationServiceImpl loanApplicationService;
	
	@GetMapping("/loans")
	public ResponseEntity<List<Loan>> getAllLoans(@RequestHeader ("Authorization") String token) throws InvalidTokenException{
		List<Loan> loans = loanService.findAllLoans(token);
		return new ResponseEntity<>(loans,HttpStatus.OK);
	}
	
	@GetMapping("/loanapplications/{loanId}")
	public ResponseEntity<List<LoanApplication>> getLoanApplicationsByLoanId(@RequestHeader ("Authorization") String token, @PathVariable("loanId") long loanId) throws LoanApplicationsNotFoundException, InvalidTokenException {
		List<LoanApplication> loanApplications = loanApplicationService.findLoanApplicationsByLoanId(token, loanId);
		return new ResponseEntity<>(loanApplications,HttpStatus.OK);
	}
	
	@GetMapping("/loanabove5")
	public ResponseEntity<List<Customer>> getAllLoanApplicationsByAmount(@RequestHeader ("Authorization") String token) throws CustomersAbove5LNotFoundException, InvalidTokenException {
		List<Customer> loanApplications = loanApplicationService.findLoanApplicationsByLoanAmount(token);
		return new ResponseEntity<>(loanApplications,HttpStatus.OK);
	}
	
	@PostMapping("/applyloan")
	public ResponseEntity<LoanApplication> applyForLoan(@RequestHeader ("Authorization") String token, @RequestBody LoanApplication application) throws InvalidTokenException {
		LoanApplication newApplication = loanApplicationService.saveNewApplication(token, application);
		return new ResponseEntity<>(newApplication,HttpStatus.CREATED);
	}
	
	
}
