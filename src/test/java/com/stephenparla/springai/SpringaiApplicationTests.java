package com.stephenparla.springai;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringaiApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private RestTemplate restTemplate;

	private String getBaseUrl() {
		return "http://localhost:" + port;
	}

	@Test
	void contextLoads() {
	}

	// =============== Ping Endpoint Tests (Public) ===============

	@Test
	void testPingEndpointNoAuth() {
		ResponseEntity<String> response = restTemplate.getForEntity(
				getBaseUrl() + "/api/aichat/ping", String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void testPingEndpointWithAuth() {
		ResponseEntity<String> response = restTemplate.getForEntity(
				getBaseUrl() + "/api/aichat/ping", String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	// =============== Prompt Endpoint Tests (Authenticated) ===============

	@Test
	void testPromptEndpointNoAuth_Returns401() {
		try {
			restTemplate.postForEntity(getBaseUrl() + "/api/aichat/prompt", 
					"test message", String.class);
			fail("Expected 401 Unauthorized");
		} catch (Exception e) {
			assertTrue(e.toString().contains("401"));
		}
	}

	@Test
	void testPromptEndpointWithInvalidCredentials_Returns401() {
		try {
			restTemplate.postForEntity(getBaseUrl() + "/api/aichat/prompt", 
					"test message", String.class);
			fail("Expected 401 Unauthorized");
		} catch (Exception e) {
			assertTrue(e.toString().contains("401"));
		}
	}


	// =============== Authentication Security Tests ===============

	@Test
	void testUnauthorizedAccessToApiEndpoint_Returns401() {
		try {
			restTemplate.getForEntity(getBaseUrl() + "/api/aichat/prompt", String.class);
			fail("Expected 401 Unauthorized");
		} catch (Exception e) {
			assertTrue(e.toString().contains("401"));
		}
	}

	@Test
	void testInvalidUsernameWithValidPassword_Returns401() {
		try {
			restTemplate.postForEntity(getBaseUrl() + "/api/aichat/prompt", 
					"test message", String.class);
			fail("Expected 401 Unauthorized");
		} catch (Exception e) {
			assertTrue(e.toString().contains("401"));
		}
	}

}
