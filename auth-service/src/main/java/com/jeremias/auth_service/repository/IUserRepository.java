package com.jeremias.auth_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jeremias.auth_service.model.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);

	boolean existsByUsername(String username);

	List<User> findByRole(String role);

	Optional<User> findByUsernameAndRole(String username, String role);
}
