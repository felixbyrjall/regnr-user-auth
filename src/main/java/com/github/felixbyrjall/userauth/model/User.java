package com.github.felixbyrjall.userauth.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

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
	@Column(nullable = false)
	private String password;

	@Email(message = "Email must be valid")
	@NotBlank(message = "Email is required")
	@Column(unique = true, nullable = false)
	private String email;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
	@Column(name = "role")
	private Set<String> roles = new HashSet<>();
}
