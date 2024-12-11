package com.github.felixbyrjall.userauth.service;

import com.github.felixbyrjall.userauth.model.User;
import com.github.felixbyrjall.userauth.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.Set;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final SecretKey secretKey;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, SecretKey secretKey) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.secretKey = secretKey;
	}

	public User registerUser(User user) {
		if (userRepository.findByUsername(user.getUsername()).isPresent()) {
			throw new RuntimeException("Username already exists");
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRoles(Set.of("USER"));
		return userRepository.save(user);
	}

	public Map<String, Object> loginUser(String username, String password) {
		System.out.println("Attempting login for username: " + username);

		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> {
					System.out.println("User not found for username: " + username);
					return new RuntimeException("User not found");
				});

		System.out.println("User found: " + user.getUsername());

		boolean matches = passwordEncoder.matches(password, user.getPassword());
		System.out.println("Password matches: " + matches);

		if (!matches) {
			throw new RuntimeException("Invalid credentials");
		}

		String token = generateToken(user);
		return Map.of(
				"token", token,
				"username", user.getUsername(),
				"userId", user.getId().toString()
		);
	}

	private String generateToken(User user) {
		return Jwts.builder()
				.setSubject(user.getUsername())
				.claim("roles", user.getRoles())
				.claim("userId", user.getId())
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 300000)) // 5 minutes
				.signWith(secretKey)
				.compact();
	}

	public User findUserByUsername(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("User not found"));
	}
}
