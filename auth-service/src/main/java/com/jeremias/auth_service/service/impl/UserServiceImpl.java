package com.jeremias.auth_service.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeremias.auth_service.model.User;
import com.jeremias.auth_service.repository.IUserRepository;
import com.jeremias.auth_service.service.IUserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements IUserService {

  private final IUserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public User createUser(User user) {
    log.info("Creating user with username: {}", user.getUsername());

    if (userRepository.existsByUsername(user.getUsername())) {
      throw new IllegalArgumentException("Username already exists: " + user.getUsername());
    }

    user.setPassword(passwordEncoder.encode(user.getPassword()));

    if (user.getRole() == null || user.getRole().isBlank()) {
      user.setRole("USER");
    }

    return userRepository.save(user);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<User> getUserById(Long id) {
    log.info("Fetching user by id: {}", id);
    return userRepository.findById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<User> getUserByUsername(String username) {
    log.info("Fetching user by username: {}", username);
    return userRepository.findByUsername(username);
  }

  @Override
  @Transactional(readOnly = true)
  public List<User> getAllUsers() {
    log.info("Fetching all users");
    return userRepository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public List<User> getUsersByRole(String role) {
    log.info("Fetching users by role: {}", role);
    return userRepository.findByRole(role);
  }

  @Override
  public User updateUser(Long id, User user) {
    log.info("Updating user with id: {}", id);
    User existingUser = userRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

    if (!existingUser.getUsername().equals(user.getUsername()) &&
        userRepository.existsByUsername(user.getUsername())) {
      throw new IllegalArgumentException("Username already exists: " + user.getUsername());
    }

    if (user.getUsername() != null && !user.getUsername().isBlank()) {
      existingUser.setUsername(user.getUsername());
    }

    if (user.getPassword() != null && !user.getPassword().isBlank()) {
      existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    if (user.getRole() != null && !user.getRole().isBlank()) {
      existingUser.setRole(user.getRole());
    }

    return userRepository.save(existingUser);
  }

  @Override
  public boolean deleteUser(Long id) {
    log.info("Deleting user with id: {}", id);
    if (!userRepository.existsById(id)) {
      return false;
    }
    userRepository.deleteById(id);
    return true;
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsByUsername(String username) {
    return userRepository.existsByUsername(username);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean validateUserCredentials(String username, String rawPassword) {
    log.info("Validating credentials for username: {}", username);
    Optional<User> userOpt = userRepository.findByUsername(username);

    if (userOpt.isEmpty()) {
      return false;
    }

    User user = userOpt.get();
    return passwordEncoder.matches(rawPassword, user.getPassword());
  }
}
