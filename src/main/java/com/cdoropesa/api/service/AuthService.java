package com.cdoropesa.api.service;

import com.cdoropesa.api.dto.AuthResponse;
import com.cdoropesa.api.dto.LoginRequest;
import com.cdoropesa.api.model.UserEntity;
import com.cdoropesa.api.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final AuthenticationManager authenticationManager;
  private final UserDetailsService userDetailsService;
  private final JwtService jwtService;

  public AuthService(
      AuthenticationManager authenticationManager,
      UserDetailsService userDetailsService,
      JwtService jwtService
  ) {
    this.authenticationManager = authenticationManager;
    this.userDetailsService = userDetailsService;
    this.jwtService = jwtService;
  }

  public AuthResponse login(LoginRequest request) {
    this.authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );

    UserDetails userDetails =
        this.userDetailsService.loadUserByUsername(request.getEmail());

    if (!(userDetails instanceof UserEntity user)) {
      throw new IllegalStateException(
          "El usuario autenticado no tiene el tipo esperado."
      );
    }

    String token = this.jwtService.generateToken(user);

    return new AuthResponse(token, user.getRole().name());
  }
}