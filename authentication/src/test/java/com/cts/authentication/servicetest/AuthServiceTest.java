package com.cts.authentication.servicetest;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.cts.authentication.model.LoginCredentials;
import com.cts.authentication.repository.AuthRepository;
import com.cts.authentication.service.MyUserDetailsService;

@SpringBootTest
public class AuthServiceTest {

	@InjectMocks
	private MyUserDetailsService myUserDetailsService;
	
	@Mock
	private AuthRepository authRepository;
	
	
	@Test
	public void Test_LoadUser_ByUsername_Success() {
		String username = "Aayushi";
		LoginCredentials login = new LoginCredentials(1,"Aayushi","pass123");
		
		UserDetails user = new User(login.getUsername(),login.getPassword(),new ArrayList<>());
		
		doReturn(login).when(authRepository).findByUserName(username);
	
		UserDetails expected = myUserDetailsService.loadUserByUsername(username);
		
		assertEquals(expected, user);
	}
	
	@Test
	public void Test_LoadUser_ByUsername_ThrowsException() {
		String username = "Aayushi";
		doReturn(null).when(authRepository).findByUserName(username);
		UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,()->myUserDetailsService.loadUserByUsername(username));
		assertEquals("Username not found", exception.getMessage());
	}
	
	@Test
	public void Test_SaveCredentials() {
		LoginCredentials login = new LoginCredentials(1,"Aayushi","pass123");
		when(authRepository.save(login)).thenReturn(login);
		when(myUserDetailsService.saveCredentials(login)).thenReturn(login);
		
		assertEquals(myUserDetailsService.saveCredentials(login),login);
		verify(authRepository, times(1)).save(login);
	}
	
	@Test
	public void Test_CustomerExistsWithUsername_Success() {
		LoginCredentials login = new LoginCredentials(1,"Aayushi","pass123");

		doReturn(login).when(authRepository).findByUserName("Aayushi");

		assertTrue(myUserDetailsService.customerExistsWithUsername(login));
	}
	
	@Test
	public void Test_CustomerExistsWithUsername_Failure() {
		LoginCredentials login = new LoginCredentials(1,"Aayushi","pass123");

		doReturn(null).when(authRepository).findByUserName("Aayushi");

		assertFalse(myUserDetailsService.customerExistsWithUsername(login));
	}
}
