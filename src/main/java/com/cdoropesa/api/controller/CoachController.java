package com.cdoropesa.api.controller;

import com.cdoropesa.api.dto.CoachRequest;
import com.cdoropesa.api.dto.CoachResponse;
import com.cdoropesa.api.generated.CoachesApi;
import com.cdoropesa.api.mapper.CoachMapper;
import com.cdoropesa.api.model.Coach;
import com.cdoropesa.api.service.CoachService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador REST para la gestión del maestro de Entrenadores del club.
 * Implementa la interfaz contract-first generada automáticamente por OpenAPI (CoachesApi).
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1")
public class CoachController implements CoachesApi {

  private final CoachService coachService;
  private final CoachMapper coachMapper;

  /**
   * Inyección de dependencias mediante constructor.
   *
   * @param coachService Servicio de lógica de negocio para entrenadores.
   * @param coachMapper Mapeador MapStruct entre entidades y DTOs.
   */
  public CoachController(CoachService coachService, CoachMapper coachMapper) {
    this.coachService = coachService;
    this.coachMapper = coachMapper;
  }

  /**
   * Obtiene el listado completo de todos los entrenadores registrados.
   *
   * @return ResponseEntity con la lista de entrenadores en formato DTO y código HTTP 200.
   */
  @Override
  public ResponseEntity<List<CoachResponse>> getAllCoaches() {
    List<Coach> coaches = this.coachService.findAllCoaches();
    return ResponseEntity.ok(this.coachMapper.toResponseList(coaches));
  }

  /**
   * Registra y crea un nuevo entrenador en el sistema.
   *
   * @param coachRequest DTO con los datos del entrenador a registrar.
   * @return ResponseEntity con el entrenador creado y código HTTP 201.
   */
  @Override
  public ResponseEntity<CoachResponse> createCoach(CoachRequest coachRequest) {
    Coach coach = this.coachMapper.toEntity(coachRequest);
    Coach saved = this.coachService.saveCoach(coach);
    return ResponseEntity.status(HttpStatus.CREATED).body(this.coachMapper.toResponse(saved));
  }
}
