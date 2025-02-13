package com.example.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

@Configuration
public class OAuthClientConfiguration {

	// Create the Okta client registration
	@Bean
	ClientRegistration oktaClientRegistration(
			@Value("${spring.security.oauth2.client.provider.okta.token-uri}") String tokenUri,
			@Value("${spring.security.oauth2.client.registration.okta.client-id}") String clientId,
			@Value("${spring.security.oauth2.client.registration.okta.client-secret}") String clientSecret,
			@Value("${spring.security.oauth2.client.registration.okta.scope}") String scope,
			@Value("${spring.security.oauth2.client.registration.okta.authorization-grant-type}") String authorizationGrantType
	) {
		return ClientRegistration.withRegistrationId("okta")
				.tokenUri(tokenUri)
				.clientId(clientId)
				.clientSecret(clientSecret)
				.scope(scope)
				.authorizationGrantType(getAuthorizationGrantType(authorizationGrantType))
				.build();
	}

	private AuthorizationGrantType getAuthorizationGrantType(String grantType) {
		return switch (grantType.toLowerCase()) {
			case "authorization_code" -> AuthorizationGrantType.AUTHORIZATION_CODE;
			case "client_credentials" -> AuthorizationGrantType.CLIENT_CREDENTIALS;
			case "password" -> AuthorizationGrantType.PASSWORD;
			case "refresh_token" -> AuthorizationGrantType.REFRESH_TOKEN;
			default -> throw new IllegalArgumentException("Unsupported grant type: " + grantType);
		};
	}

	// Create the client registration repository
	@Bean(name = "customClientRegistrationRepository")
	public ClientRegistrationRepository clientRegistrationRepository(ClientRegistration oktaClientRegistration) {
		return new InMemoryClientRegistrationRepository(oktaClientRegistration);
	}

	// Create the authorized client service
	@Bean
	public OAuth2AuthorizedClientService oAuth2AuthorizedClientService(ClientRegistrationRepository clientRegistrationRepository) {
		return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
	}

	// Create the authorized client manager and service manager using the beans created and configured above
	@Bean
	public OAuth2AuthorizedClientManager authorizedClientManager(
			ClientRegistrationRepository clientRegistrationRepository,
			OAuth2AuthorizedClientService authorizedClientService) {

		OAuth2AuthorizedClientProvider authorizedClientProvider =
				OAuth2AuthorizedClientProviderBuilder.builder()
						.clientCredentials() // Ensure correct flow handling
						.build();

		AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager =
				new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientService);
		authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

		return authorizedClientManager;
	}
}
