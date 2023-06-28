package com.cts.loan.service;

import java.util.List;

import com.cts.loan.exceptions.InvalidTokenException;
import com.cts.loan.model.Loan;

public interface LoanService {

	public List<Loan> findAllLoans(String token) throws InvalidTokenException;
	
}
