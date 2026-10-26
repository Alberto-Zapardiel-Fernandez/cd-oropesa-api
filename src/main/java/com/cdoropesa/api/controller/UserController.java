package com.cdoropesa.api.controller;

import com.cdoropesa.api.model.UserEntity;
import com.cdoropesa.api.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador REST para la gestión de endpoints relacionados con los usuarios.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  public ResponseEntity<List<UserEntity>> getAllUsers() {
    return ResponseEntity.ok(this.userService.getAllUsers());
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserEntity> getUserById(@PathVariable Long id) {
    return ResponseEntity.ok(this.userService.getUserById(id));
  }
}
