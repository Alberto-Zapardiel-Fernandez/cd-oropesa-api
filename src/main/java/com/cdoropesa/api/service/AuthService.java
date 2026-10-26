package com.cdoropesa.api.service;

import com.cdoropesa.api.dto.AuthResponse;
import com.cdoropesa.api.dto.LoginRequest;
import com.cdoropesa.api.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * Servicio encargado de gestionar la lógica de negocio de la autenticación.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Service
public class AuthService {

  private final AuthenticationManager authenticationManager;
  private final UserDetailsService userDetailsService;
  private final JwtService jwtService;

  /**
   * Inyección por constructor de las dependencias de autenticación y tokens.
   *
   * @param authenticationManager Gestor de autenticación de Spring.
   * @param userDetailsService Servicio de carga de usuarios.
   * @param jwtService Servicio de generación de JWT.
   */
  public AuthService(AuthenticationManager authenticationManager,
                     UserDetailsService userDetailsService,
                     JwtService jwtService) {
    this.authenticationManager = authenticationManager;
    this.userDetailsService = userDetailsService;
    this.jwtService = jwtService;
  }

  /**
   * Autentica a un usuario y genera su token JWT correspondiente.
   *
   * @param request Credenciales de acceso (email y contraseña).
   * @return AuthResponse con el token JWT.
   */
  public AuthResponse login(LoginRequest request) {
    this.authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );
    UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getEmail());
    String token = this.jwtService.generateToken(userDetails);
    return new AuthResponse(token);
  }
}
