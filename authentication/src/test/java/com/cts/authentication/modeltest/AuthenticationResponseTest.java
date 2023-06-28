package com.cts.authentication.modeltest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.cts.authentication.model.AuthenticationResponse;

@SpringBootTest
public class AuthenticationResponseTest {

	AuthenticationResponse response = new AuthenticationResponse();
	
	@Test
	public void Test_Jwt() {
		response.setJwt("token");
		assertEquals("token",response.getJwt());
	}
}
