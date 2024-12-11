package com.github.felixbyrjall.userauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserAuthApplication {
	public static void main(String[] args) {
		SpringApplication.run(UserAuthApplication.class, args);
		System.out.println("H2 Console URL: http://localhost:8082/h2-console-user-auth");
		System.out.println("JDBC URL: jdbc:h2:file:./data/user-auth-db");
	}
}
