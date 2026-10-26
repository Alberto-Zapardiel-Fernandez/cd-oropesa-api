package com.cdoropesa.api.controller;

import com.cdoropesa.api.dto.AuthResponse;
import com.cdoropesa.api.dto.LoginRequest;
import com.cdoropesa.api.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para gestionar la autenticación y emisión de tokens.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final AuthService authService;

  /**
   * Inyección por constructor del servicio de autenticación.
   *
   * @param authService Servicio de lógica de login.
   */
  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  /**
   * Endpoint público para iniciar sesión y obtener un token JWT.
   *
   * @param request Credenciales del usuario.
   * @return ResponseEntity con el token JWT.
   */
  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
    return ResponseEntity.ok(this.authService.login(request));
  }
}
