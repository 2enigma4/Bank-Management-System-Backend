package com.cts.authentication.utiltest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.cts.authentication.util.JwtUtil;

@SpringBootTest
public class JwtUtilTest {

	@InjectMocks
	JwtUtil jwtUtil;
	
	@Mock
	UserDetails userdetails;
	

	
	
	@Test
	 void generateTokenTest() {
		userdetails = new User("grace", "grace", new ArrayList<>());
		String generateToken = jwtUtil.generateToken(userdetails);
		assertNotNull(generateToken);
	}
	
	@Test
	 void validateTokenTest() {
		userdetails = new User("grace", "grace", new ArrayList<>());
		String generateToken = jwtUtil.generateToken(userdetails);
		Boolean validateToken = jwtUtil.validateToken(generateToken);
		assertEquals(true, validateToken);
	}
	
	@Test
	 void extractUsernameTest() {
		userdetails = new User("grace", "grace", new ArrayList<>());
		String generateToken = jwtUtil.generateToken(userdetails);
		String username = jwtUtil.extractUsername(generateToken);
		assertEquals("grace", username);
	}
	
	@Test
	 void extractExpirationTest() {
		userdetails = new User("grace", "grace", new ArrayList<>());
		String generateToken = jwtUtil.generateToken(userdetails);
		Date actual = jwtUtil.extractExpiration(generateToken);
		Date expected = new Date(System.currentTimeMillis() + 600000);
		assertEquals(expected.toString(),actual.toString());
	}
	
}
