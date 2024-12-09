package com.github.felixbyrjall.userauth.repository;
import com.github.felixbyrjall.userauth.model.User;

import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface UserRepository extends ListCrudRepository<User, Long> {
	Optional<User> findByUsername(String username);
}
