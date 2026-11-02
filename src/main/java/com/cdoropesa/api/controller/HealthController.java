package com.cdoropesa.api.controller;

import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Public readiness endpoint for hosting and availability monitors.
 */
@RestController
@RequestMapping("/api/v1/health")
public class HealthController {

  private final JdbcTemplate jdbcTemplate;

  public HealthController(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @GetMapping
  public ResponseEntity<Map<String, Object>> checkHealth() {
    this.jdbcTemplate.queryForObject("SELECT 1", Integer.class);
    Map<String, Object> response = Map.of(
        "status", "UP",
        "service", "CD Oropesa API"
    );
    return ResponseEntity.ok(response);
  }
}
