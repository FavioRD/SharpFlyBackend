package com.jeremias.auth_service.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jeremias.auth_service.model.User;
import com.jeremias.auth_service.service.IUserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

  private final IUserService userService;

  @PostMapping
  public ResponseEntity<User> createUser(@RequestBody User user) {
    log.info("POST /api/v1/users - Creating user: {}", user.getUsername());
    try {
      User created = userService.createUser(user);
      return ResponseEntity.status(HttpStatus.CREATED).body(created);
    } catch (IllegalArgumentException e) {
      log.error("Error creating user: {}", e.getMessage());
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> getUserById(@PathVariable Long id) {
    log.info("GET /api/v1/users/{} - Fetching user", id);
    return userService.getUserById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/username/{username}")
  public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
    log.info("GET /api/v1/users/username/{} - Fetching user by username", username);
    return userService.getUserByUsername(username)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping
  public ResponseEntity<List<User>> getAllUsers(
      @RequestParam(required = false) String role) {
    log.info("GET /api/v1/users - Fetching all users, role filter: {}", role);
    if (role != null && !role.isBlank()) {
      return ResponseEntity.ok(userService.getUsersByRole(role));
    }
    return ResponseEntity.ok(userService.getAllUsers());
  }

  @PutMapping("/{id}")
  public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
    log.info("PUT /api/v1/users/{} - Updating user", id);
    try {
      User updated = userService.updateUser(id, user);
      return ResponseEntity.ok(updated);
    } catch (IllegalArgumentException e) {
      log.error("Error updating user: {}", e.getMessage());
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    log.info("DELETE /api/v1/users/{} - Deleting user", id);
    boolean deleted = userService.deleteUser(id);
    return deleted
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }

  @GetMapping("/exists/username/{username}")
  public ResponseEntity<Boolean> existsByUsername(@PathVariable String username) {
    log.info("GET /api/v1/users/exists/username/{} - Checking existence", username);
    return ResponseEntity.ok(userService.existsByUsername(username));
  }

  @PostMapping("/validate")
  public ResponseEntity<Boolean> validateCredentials(
      @RequestParam String username,
      @RequestParam String password) {
    log.info("POST /api/v1/users/validate - Validating credentials for: {}", username);
    boolean valid = userService.validateUserCredentials(username, password);
    return ResponseEntity.ok(valid);
  }
}
