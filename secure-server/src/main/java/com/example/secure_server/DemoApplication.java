package com.example.secureserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.okta.spring.boot.oauth.Okta;
import java.security.Principal;
import org.springframework.security.access.prepost.PreAuthorize;

@SpringBootApplication
@EnableMethodSecurity(prePostEnabled = true)
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated()).oauth2ResourceServer(oauth2 -> oauth2.jwt());
		return http.build();
	}

	@RestController
	public class RequestController {
		@GetMapping("/")
		@PreAuthorize("hasAuthority('SCOPE_mod_custom')")
		public String getMessage(Principal principal) {
			System.out.println("Welcome, " + principal.getName());
			return "Welcome, " + principal.getName();
		}
	}
}
