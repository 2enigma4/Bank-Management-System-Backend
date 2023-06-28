package com.cts.loan.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.loan.model.ValidatedResponse;
import com.cts.loan.exceptions.CustomersAbove5LNotFoundException;
import com.cts.loan.exceptions.InvalidTokenException;
import com.cts.loan.exceptions.LoanApplicationsNotFoundException;
import com.cts.loan.model.Customer;
import com.cts.loan.model.LoanApplication;
import com.cts.loan.repository.LoanApplicationRepository;
import com.cts.loan.restclient.AuthenticationClient;
import com.cts.loan.restclient.CustomerClient;

@Service
public class LoanApplicationServiceImpl implements LoanApplicationService {

	@Autowired
	private LoanApplicationRepository loanApplicationRepo;

	@Autowired
	private CustomerClient customerClient;

	@Autowired
	private AuthenticationClient authClient;

	// To Save New Application
	public LoanApplication saveNewApplication(String token, LoanApplication application2) throws InvalidTokenException {

		ValidatedResponse body = authClient.getValidity(token).getBody();
		if (body != null && body.isValid()) {
			return loanApplicationRepo.save(application2);
		} else {
			throw new InvalidTokenException();
		}
	}

	public List<LoanApplication> findAllLoanApplications() {
		return loanApplicationRepo.findAll();
	}

	public List<LoanApplication> findLoanApplicationsByLoanId(String token, long loanId)
			throws LoanApplicationsNotFoundException, InvalidTokenException {

		ValidatedResponse body = authClient.getValidity(token).getBody();
		if (body != null && body.isValid()) {
			List<LoanApplication> loanApplications = loanApplicationRepo.getLoanApplicationByLoanId(loanId);
			if (loanApplications.isEmpty()) {
				throw new LoanApplicationsNotFoundException();
			}
			return loanApplications;
		} else {
			throw new InvalidTokenException();
		}
	}

	// customer loan greater than 5L
	public List<Customer> findLoanApplicationsByLoanAmount(String token)
			throws CustomersAbove5LNotFoundException, InvalidTokenException {

		ValidatedResponse body = authClient.getValidity(token).getBody();
		if (body != null && body.isValid()) {
			List<Long> loanApplications = new ArrayList<>();

			findAllLoanApplications().stream().filter(app -> app.getAmount() > 500000)
					.forEach(app -> loanApplications.add(app.getCustomerId()));

			if (!loanApplications.isEmpty()) {

				List<Customer> custList = new ArrayList<>();
				for (Long custId : loanApplications) {

					custList.add(customerClient.getCustomer(token, custId));
				}
				
				return custList;
			} else {
				throw new CustomersAbove5LNotFoundException();
			}

		} else {
			throw new InvalidTokenException();
			
		}

	}

}
