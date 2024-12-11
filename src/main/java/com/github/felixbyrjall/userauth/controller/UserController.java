package com.github.felixbyrjall.userauth.controller;

import com.github.felixbyrjall.userauth.model.User;
import com.github.felixbyrjall.userauth.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class UserController {
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody User user) {
		try {
			System.out.println("Login request payload: " + user.getUsername());
			String token = userService.loginUser(user.getUsername(), user.getPassword());

			return ResponseEntity.ok(Map.of(
					"token", token,
					"username", user.getUsername()
			));
		} catch (Exception e) {
			System.err.println("Error during login: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Map.of("error", "Invalid credentials"));
		}
	}

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
		try {
			User registeredUser = userService.registerUser(user);
			return ResponseEntity.ok(Map.of(
					"message", "User registered successfully",
					"username", registeredUser.getUsername()
			));
		} catch (Exception e) {
			System.err.println("Error during registration: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Map.of("error", e.getMessage()));
		}
	}
}
