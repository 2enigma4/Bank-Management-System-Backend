package com.cts.loan.service;

import java.util.List;

import com.cts.loan.exceptions.CustomersAbove5LNotFoundException;
import com.cts.loan.exceptions.InvalidTokenException;
import com.cts.loan.exceptions.LoanApplicationsNotFoundException;
import com.cts.loan.model.Customer;
import com.cts.loan.model.LoanApplication;

public interface LoanApplicationService {

	public LoanApplication saveNewApplication(String token, LoanApplication application2)throws InvalidTokenException;
	
	public List<LoanApplication> findAllLoanApplications();
	
	public List<LoanApplication> findLoanApplicationsByLoanId(String token, long loanId) throws LoanApplicationsNotFoundException, InvalidTokenException;
	
	public List<Customer> findLoanApplicationsByLoanAmount(String token) throws CustomersAbove5LNotFoundException, InvalidTokenException;
}
