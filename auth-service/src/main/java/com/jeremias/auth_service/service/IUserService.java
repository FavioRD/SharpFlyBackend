package com.jeremias.auth_service.service;

import java.util.List;
import java.util.Optional;

import com.jeremias.auth_service.model.User;

public interface IUserService {

  User createUser(User user);

  Optional<User> getUserById(Long id);

  Optional<User> getUserByUsername(String username);

  List<User> getAllUsers();

  List<User> getUsersByRole(String role);

  User updateUser(Long id, User user);

  boolean deleteUser(Long id);

  boolean existsByUsername(String username);

  boolean validateUserCredentials(String username, String rawPassword);
}
