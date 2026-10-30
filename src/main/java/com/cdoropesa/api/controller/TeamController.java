package com.cdoropesa.api.controller;

import com.cdoropesa.api.dto.TeamRequest;
import com.cdoropesa.api.dto.TeamResponse;
import com.cdoropesa.api.generated.TeamsApi;
import com.cdoropesa.api.mapper.TeamMapper;
import com.cdoropesa.api.model.Category;
import com.cdoropesa.api.model.Season;
import com.cdoropesa.api.model.Team;
import com.cdoropesa.api.service.CategoryService;
import com.cdoropesa.api.service.SeasonService;
import com.cdoropesa.api.service.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * Controlador REST para la gestión de Equipos federados por temporada.
 * Implementa la interfaz contract-first generada automáticamente por OpenAPI (TeamsApi).
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1")
public class TeamController implements TeamsApi {

  private final TeamService teamService;
  private final CategoryService categoryService;
  private final SeasonService seasonService;
  private final TeamMapper teamMapper;

  /**
   * Inyección de dependencias mediante constructor.
   *
   * @param teamService Servicio de lógica de negocio para equipos.
   * @param categoryService Servicio de categorías para resolver relaciones.
   * @param seasonService Servicio de temporadas para resolver relaciones.
   * @param teamMapper Mapeador MapStruct entre entidades y DTOs.
   */
  public TeamController(TeamService teamService,
                        CategoryService categoryService,
                        SeasonService seasonService,
                        TeamMapper teamMapper) {
    this.teamService = teamService;
    this.categoryService = categoryService;
    this.seasonService = seasonService;
    this.teamMapper = teamMapper;
  }

  /**
   * Obtiene el listado completo de todos los equipos registrados.
   *
   * @return ResponseEntity con la lista de equipos en formato DTO y código HTTP 200.
   */
  @Override
  public ResponseEntity<List<TeamResponse>> getAllTeams() {
    List<Team> teams = this.teamService.findAllTeams();
    return ResponseEntity.ok(this.teamMapper.toResponseList(teams));
  }

  /**
   * DELETE /teams/{id}: Eliminar un equipo
   *
   * @param id (required)
   * @return Equipo eliminado con éxito (status code 204)
   */
  @Override
  public ResponseEntity<Void> teamsIdDelete(UUID id) {
    teamService.deleteTeam(id);
    return ResponseEntity.noContent().build();
  }

  /**
   * Registra y crea un nuevo equipo vinculándolo a su categoría y temporada correspondientes.
   *
   * @param teamRequest DTO con los datos del equipo a crear.
   * @return ResponseEntity con el equipo creado y código HTTP 201, o 400 si no existen la categoría o temporada.
   */
  @Override
  public ResponseEntity<TeamResponse> createTeam(TeamRequest teamRequest) {
    Category category = this.categoryService.findCategoryById(teamRequest.getCategoryId()).orElse(null);
    Season season = this.seasonService.findSeasonById(teamRequest.getSeasonId()).orElse(null);

    if (category == null || season == null) {
      return ResponseEntity.badRequest().build();
    }

    Team team = this.teamMapper.toEntity(teamRequest, category, season);
    Team saved = this.teamService.saveTeam(team);
    return ResponseEntity.status(HttpStatus.CREATED).body(this.teamMapper.toResponse(saved));
  }
}
