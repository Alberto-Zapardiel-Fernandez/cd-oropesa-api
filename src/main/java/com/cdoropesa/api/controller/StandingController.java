package com.cdoropesa.api.controller;

import com.cdoropesa.api.dto.StandingResponse;
import com.cdoropesa.api.generated.StandingsApi;
import com.cdoropesa.api.mapper.StandingMapper;
import com.cdoropesa.api.model.Standing;
import com.cdoropesa.api.service.StandingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador REST para la consulta de las tablas de clasificación de la competición.
 * Implementa la interfaz contract-first generada automáticamente por OpenAPI (StandingsApi).
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1")
public class StandingController implements StandingsApi {

  private final StandingService standingService;
  private final StandingMapper standingMapper;

  /**
   * Inyección de dependencias mediante constructor.
   *
   * @param standingService Servicio de lógica de negocio para clasificaciones.
   * @param standingMapper Mapeador MapStruct entre entidades y DTOs.
   */
  public StandingController(StandingService standingService, StandingMapper standingMapper) {
    this.standingService = standingService;
    this.standingMapper = standingMapper;
  }

  /**
   * Obtiene la tabla de clasificación completa con estadísticas y puntuaciones.
   *
   * @return ResponseEntity con la lista de registros de clasificación en formato DTO y código HTTP 200.
   */
  @Override
  public ResponseEntity<List<StandingResponse>> getAllStandings() {
    List<Standing> standings = this.standingService.findAllStandings();
    return ResponseEntity.ok(this.standingMapper.toResponseList(standings));
  }
}
