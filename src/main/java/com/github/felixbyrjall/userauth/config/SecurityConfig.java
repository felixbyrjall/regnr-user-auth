package com.github.felixbyrjall.userauth.config;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Base64;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Value("${jwt.secret-key}")
	private String base64EncodedKey;

	@Bean
	public SecretKey secretKey() {
		if (base64EncodedKey == null || base64EncodedKey.isEmpty()) {
			throw new IllegalArgumentException("JWT secret key is not configured.");
		}
		byte[] decodedKey = Base64.getDecoder().decode(base64EncodedKey);
		return Keys.hmacShaKeyFor(decodedKey);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				//.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.csrf(csrf -> csrf
						.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
						.ignoringRequestMatchers(
								"/h2-console-user-auth/**",
								"/actuator/health",
								"/api/auth/register",
								"/api/csrf",
								"/api/test-auth"
						)
				)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> {
					auth.requestMatchers("/h2-console-user-auth/**").permitAll();
					auth.requestMatchers("/api/auth/login", "/api/auth/register", "/api/csrf", "/api/test-auth").permitAll();
					auth.requestMatchers("/api/admin/**").hasAuthority("ADMIN");
					auth.requestMatchers("/actuator/health").permitAll();
					auth.anyRequest().authenticated();
				})
				.headers(headers -> headers
						.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
				)
				.addFilterBefore(new JwtAuthenticationFilter(secretKey()), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:8000"));
		config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		config.setAllowedHeaders(Arrays.asList(
				"Content-Type",
				"X-XSRF-TOKEN",
				"Authorization",
				"X-Requested-With",
				"Accept",
				"Origin",
				"X-User-Id"
		));
		config.setExposedHeaders(Arrays.asList("X-XSRF-TOKEN"));
		config.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
}
