package com.cdoropesa.api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración principal de Spring Security optimizada para Spring Boot 4.x
 * con soporte para autenticación JWT stateless.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final CorsConfig corsConfig;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final AuthenticationProvider authenticationProvider;

  /**
   * Inyección por constructor de dependencias de seguridad.
   *
   * @param corsConfig Configuración de orígenes cruzados.
   * @param jwtAuthenticationFilter Filtro de validación de tokens JWT.
   * @param authenticationProvider Proveedor de autenticación de credenciales.
   */
  public SecurityConfig(CorsConfig corsConfig,
                        JwtAuthenticationFilter jwtAuthenticationFilter,
                        AuthenticationProvider authenticationProvider) {
    this.corsConfig = corsConfig;
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    this.authenticationProvider = authenticationProvider;
  }

  /**
   * Configura la cadena de filtros de seguridad HTTP, integrando JWT de forma stateless.
   *
   * @param http Objeto HttpSecurity para configurar reglas de seguridad.
   * @return El filtro de seguridad construido.
   * @throws Exception Si ocurre algún error durante la configuración.
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors(cors -> cors.configurationSource(this.corsConfig.corsConfigurationSource()))
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(this.authenticationProvider)
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .requestMatchers("/api/v1/auth/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/v1/players", "/api/v1/players/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET, "/api/v1/coaches", "/api/v1/coaches/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET, "/api/v1/users", "/api/v1/users/**").hasRole("ADMIN")
            .requestMatchers(
                HttpMethod.GET,
                "/api/v1/teams/*/players",
                "/api/v1/teams/*/players/**"
            ).hasRole("ADMIN")
            .requestMatchers(
                HttpMethod.GET,
                "/api/v1/teams/*/staff",
                "/api/v1/teams/*/staff/**"
            ).hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET, "/api/v1/**").authenticated()
            .requestMatchers(HttpMethod.POST, "/api/v1/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/api/v1/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PATCH, "/api/v1/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/v1/**").hasRole("ADMIN")
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
            .anyRequest().authenticated()
        )
        .addFilterBefore(this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
