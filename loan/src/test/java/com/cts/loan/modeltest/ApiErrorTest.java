package com.cts.loan.modeltest;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.cts.loan.model.ApiError;

@SpringBootTest
public class ApiErrorTest {

	LocalDateTime date = LocalDateTime.now();
	ApiError error = new ApiError();
	
	@Test
	public void Test_getStatus() {
		error.setStatus(HttpStatus.OK);
		assertEquals(HttpStatus.OK,error.getStatus());
	}
	
	@Test
	public void Test_getTimeStamp() {
		error.setTimestamp(date);
		assertEquals(date,error.getTimestamp());
	}
	
	@Test
	public void Test_getMessage() {
		error.setMessage("error");
		assertEquals("error",error.getMessage());
	}
	
	@Test
	public void Test_toString() {
		assertEquals("ApiError(status=null, timestamp=null, message=null)",error.toString());
	}
}
