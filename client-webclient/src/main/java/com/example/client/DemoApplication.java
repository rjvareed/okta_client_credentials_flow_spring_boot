package com.example.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Configuration
@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	// Inject the OAuth2AuthorizedClientManager instead of AuthorizedClientServiceOAuth2AuthorizedClientManager
	@Autowired
	private OAuth2AuthorizedClientManager authorizedClientManager;

	@Override
	public void run(String... args) throws Exception {

		////////////////////////////////////////////////////
		//  STEP 1: Retrieve the authorized JWT
		////////////////////////////////////////////////////

		// Build an OAuth2 request for the Okta provider
		OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest.withClientRegistrationId("okta")
				.principal("Demo Service")
				.build();

		// Perform authorization and retrieve OAuth2AuthorizedClient
		OAuth2AuthorizedClient authorizedClient = authorizedClientManager.authorize(authorizeRequest);

		if (authorizedClient == null || authorizedClient.getAccessToken() == null) {
			logger.error("Failed to retrieve access token.");
			return;
		}

		// Get the access token
		OAuth2AccessToken accessToken = authorizedClient.getAccessToken();

		logger.info("Issued: " + accessToken.getIssuedAt() + ", Expires: " + accessToken.getExpiresAt());
		logger.info("Scopes: " + accessToken.getScopes());
		logger.info("Token: " + accessToken.getTokenValue());

		////////////////////////////////////////////////////
		//  STEP 2: Use the JWT and call the service
		////////////////////////////////////////////////////

		// Add the JWT to the RestTemplate headers
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + accessToken.getTokenValue());
		HttpEntity<String> request = new HttpEntity<>(headers);

		// Make the actual HTTP GET request
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.exchange(
				"http://localhost:8080",
				HttpMethod.GET,
				request,
				String.class
		);

		String result = response.getBody();
		logger.info("Reply = " + result);
	}
}
