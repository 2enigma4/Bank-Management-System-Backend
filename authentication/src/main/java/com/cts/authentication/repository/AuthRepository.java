package com.cts.authentication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cts.authentication.model.LoginCredentials;

@Repository
public interface AuthRepository extends JpaRepository<LoginCredentials,Integer> {
	
	@Query(value = "SELECT u FROM LoginCredentials u WHERE u.username = ?1")
	LoginCredentials findByUserName(String username);
}
