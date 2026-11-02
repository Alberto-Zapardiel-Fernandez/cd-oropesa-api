package com.cdoropesa.api.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Configuración global de CORS (Cross-Origin Resource Sharing) para permitir
 * la comunicación segura con futuros clientes (frontend web/móvil).
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Configuration
public class CorsConfig {

  private final String allowedOrigins;

  public CorsConfig(@Value("${application.cors.allowed-origins}") String allowedOrigins) {
    this.allowedOrigins = allowedOrigins;
  }

  /**
   * Define la fuente de configuración CORS para la aplicación.
   *
   * @return CorsConfigurationSource con las políticas de origen, métodos y cabeceras permitidas.
   */
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.stream(this.allowedOrigins.split(","))
        .map(String::trim)
        .filter(origin -> !origin.isEmpty())
        .toList());
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
    configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/api/**", configuration);
    return source;
  }
}
