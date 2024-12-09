package com.github.felixbyrjall.userauth.controller;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class CsrfController {

	@GetMapping("/api/csrf")
	public CsrfToken csrf(CsrfToken token) {
		log.info("Generated CSRF Token: {}", token.getToken());
		return token;
	}
}
