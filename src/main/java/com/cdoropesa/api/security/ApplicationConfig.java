package com.cdoropesa.api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuración central de los beans de seguridad, cifrado de contraseñas
 * y proveedores de autenticación adaptada a Spring Boot 4.x.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Configuration
public class ApplicationConfig {

  private final UserDetailsService userDetailsService;

  /**
   * Inyección por constructor del servicio de detalles de usuario.
   *
   * @param userDetailsService Servicio encargado de buscar usuarios en el sistema.
   */
  public ApplicationConfig(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  /**
   * Define el codificador de contraseñas utilizando el algoritmo seguro BCrypt.
   *
   * @return Instancia de PasswordEncoder.
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Configura el proveedor de autenticación pasando el UserDetailsService por constructor
   * tal y como exige esta versión, y asociando el PasswordEncoder mediante su setter.
   *
   * @return AuthenticationProvider configurado.
   */
  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(this.userDetailsService);
    authProvider.setPasswordEncoder(this.passwordEncoder());
    return authProvider;
  }

  /**
   * Expone el AuthenticationManager global requerido para el proceso de Login.
   *
   * @param config Configuración de autenticación de Spring.
   * @return Gestor de autenticación.
   * @throws Exception Si ocurre algún error en la inicialización.
   */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }
}