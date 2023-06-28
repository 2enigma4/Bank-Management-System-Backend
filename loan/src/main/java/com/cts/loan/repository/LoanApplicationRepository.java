package com.cts.loan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cts.loan.model.LoanApplication;

@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long>{

	@Query(value = "select u from LoanApplication u where u.loanId = ?1")
	public List<LoanApplication> getLoanApplicationByLoanId(long loanId);
	
}
