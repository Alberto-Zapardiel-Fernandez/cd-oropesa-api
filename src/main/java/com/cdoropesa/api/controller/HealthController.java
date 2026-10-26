package com.cdoropesa.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Controlador para verificar el estado de salud de la API (Health Check).
 * Utilizado habitualmente por servicios externos como Render para evitar que la instancia se duerma.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/health")
public class HealthController {

  /**
   * Endpoint público para comprobar que la API está activa.
   *
   * @return ResponseEntity con el estado y timestamp actual.
   */
  @GetMapping
  public ResponseEntity<Map<String, Object>> checkHealth() {
    Map<String, Object> response = Map.of(
        "status", "UP",
        "service", "CD Oropesa API",
        "timestamp", LocalDateTime.now().toString()
    );
    return ResponseEntity.ok(response);
  }
}
