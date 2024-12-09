package com.github.felixbyrjall.userauth.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Username is required")
	@Column(unique = true, nullable = false) // Enforce non-null constraint at the database level
	private String username;

	@NotBlank(message = "Password is required")
	@Column(nullable = false) // Enforce non-null constraint
	private String password;

	@Email(message = "Email must be valid")
	@NotBlank(message = "Email is required") // Ensure email is not blank
	@Column(unique = true, nullable = false) // Add uniqueness and non-null constraints
	private String email;
}
