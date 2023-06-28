package com.cts.loan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.loan.exceptions.InvalidTokenException;
import com.cts.loan.model.Loan;
import com.cts.loan.model.ValidatedResponse;
import com.cts.loan.repository.LoanRepository;
import com.cts.loan.restclient.AuthenticationClient;

@Service
public class LoanServiceImpl implements LoanService {

	@Autowired
	private LoanRepository loanRepo;

	@Autowired
	private AuthenticationClient authClient;

	public List<Loan> findAllLoans(String token) throws InvalidTokenException {
		ValidatedResponse body = authClient.getValidity(token).getBody();
		if (body != null && body.isValid()) {
			return loanRepo.findAll();
			
		} else {
			throw new InvalidTokenException();
		}

	}
}
