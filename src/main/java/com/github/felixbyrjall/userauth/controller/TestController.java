package com.github.felixbyrjall.userauth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TestController {
	@GetMapping("/api/test-auth")
	public ResponseEntity<?> testAuth() {
		System.out.println("Test auth endpoint called");
		return ResponseEntity.ok(Map.of("message", "Auth test successful"));
	}
}
