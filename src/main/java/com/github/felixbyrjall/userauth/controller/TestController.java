package com.github.felixbyrjall.userauth.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class TestController {
	@GetMapping("/test-auth")
	public ResponseEntity<?> testAuth(HttpServletRequest request) {
		log.error("Test auth endpoint CALLED");
		log.error("Full Request URI: " + request.getRequestURI());
		log.error("Full Request URL: " + request.getRequestURL());
		log.error("Request Method: " + request.getMethod());

		return ResponseEntity.ok(Map.of("message", "Auth test successful"));
	}
}
