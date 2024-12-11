package com.github.felixbyrjall.userauth.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final SecretKey secretKey;

	public JwtAuthenticationFilter(SecretKey secretKey) {
		this.secretKey = secretKey;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authHeader = request.getHeader("Authorization");

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			try {
				String token = authHeader.substring(7);
				System.out.println("Validating token: " + token.substring(0, Math.min(10, token.length())) + "...");

				Claims claims = Jwts.parserBuilder()
						.setSigningKey(secretKey)
						.build()
						.parseClaimsJws(token)
						.getBody();

				System.out.println("Token expiration: " + claims.getExpiration());
				System.out.println("Current time: " + new Date());

				String username = claims.getSubject();
				List<String> roles = claims.get("roles", List.class);

				List<SimpleGrantedAuthority> authorities = roles.stream()
						.map(SimpleGrantedAuthority::new)
						.collect(Collectors.toList());

				UsernamePasswordAuthenticationToken authentication =
						new UsernamePasswordAuthenticationToken(username, null, authorities);

				SecurityContextHolder.getContext().setAuthentication(authentication);
			} catch (Exception e) {
				System.out.println("Token validation failed: " + e.getMessage());
				SecurityContextHolder.clearContext();
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}
		}

		filterChain.doFilter(request, response);
	}
}
