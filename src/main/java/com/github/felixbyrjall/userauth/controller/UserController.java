package com.github.felixbyrjall.userauth.controller;

import com.github.felixbyrjall.userauth.model.User;
import com.github.felixbyrjall.userauth.service.UserService;
import jakarta.validation.Valid;
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

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
		User registeredUser = userService.registerUser(user);
		return ResponseEntity.ok(Map.of("message", "User registered successfully", "username", registeredUser.getUsername()));
	}

	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody User user) {
		User authenticatedUser = userService.loginUser(user.getUsername(), user.getPassword());
		return ResponseEntity.ok("Welcome, " + authenticatedUser.getUsername());
	}
}
