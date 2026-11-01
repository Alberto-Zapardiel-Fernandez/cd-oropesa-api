package com.cdoropesa.api.controller;

import com.cdoropesa.api.dto.TeamRequest;
import com.cdoropesa.api.dto.TeamResponse;
import com.cdoropesa.api.dto.TeamPlayerCreateRequest;
import com.cdoropesa.api.dto.TeamPlayerResponse;
import com.cdoropesa.api.dto.TeamPlayerUpdateRequest;
import com.cdoropesa.api.generated.TeamsApi;
import com.cdoropesa.api.mapper.TeamPlayerMapper;
import com.cdoropesa.api.mapper.TeamMapper;
import com.cdoropesa.api.model.Category;
import com.cdoropesa.api.model.Player;
import com.cdoropesa.api.model.Season;
import com.cdoropesa.api.model.Team;
import com.cdoropesa.api.model.TeamPlayer;
import com.cdoropesa.api.model.enums.PlayerStatus;
import com.cdoropesa.api.service.CategoryService;
import com.cdoropesa.api.service.PlayerService;
import com.cdoropesa.api.service.SeasonService;
import com.cdoropesa.api.service.TeamService;
import com.cdoropesa.api.service.TeamPlayerService;
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
  private final TeamPlayerService teamPlayerService;
  private final PlayerService playerService;
  private final CategoryService categoryService;
  private final SeasonService seasonService;
  private final TeamMapper teamMapper;
  private final TeamPlayerMapper teamPlayerMapper;

  /**
   * Inyección de dependencias mediante constructor.
   *
   * @param teamService Servicio de lógica de negocio para equipos.
   * @param categoryService Servicio de categorías para resolver relaciones.
   * @param seasonService Servicio de temporadas para resolver relaciones.
   * @param teamMapper Mapeador MapStruct entre entidades y DTOs.
   */
  public TeamController(TeamService teamService,
                        TeamPlayerService teamPlayerService,
                        PlayerService playerService,
                        CategoryService categoryService,
                        SeasonService seasonService,
                        TeamMapper teamMapper,
                        TeamPlayerMapper teamPlayerMapper) {
    this.teamService = teamService;
    this.teamPlayerService = teamPlayerService;
    this.playerService = playerService;
    this.categoryService = categoryService;
    this.seasonService = seasonService;
    this.teamMapper = teamMapper;
    this.teamPlayerMapper = teamPlayerMapper;
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

  @Override
  public ResponseEntity<TeamResponse> getTeamById(UUID id) {
    return this.teamService.findTeamById(id)
        .map(team -> ResponseEntity.ok(this.teamMapper.toResponse(team)))
        .orElse(ResponseEntity.notFound().build());
  }

  @Override
  public ResponseEntity<TeamResponse> updateTeam(UUID id, TeamRequest teamRequest) {
    return this.teamService.findTeamById(id)
        .map(existingTeam -> {
          Category category = this.categoryService
              .findCategoryById(teamRequest.getCategoryId()).orElse(null);
          Season season = this.seasonService
              .findSeasonById(teamRequest.getSeasonId()).orElse(null);
          if (category == null || season == null) {
            return ResponseEntity.badRequest().<TeamResponse>build();
          }

          Team updatedTeam = this.teamMapper.toEntity(teamRequest, category, season);
          updatedTeam.setId(existingTeam.getId());
          updatedTeam.setCreatedAt(existingTeam.getCreatedAt());
          Team saved = this.teamService.saveTeam(updatedTeam);
          return ResponseEntity.ok(this.teamMapper.toResponse(saved));
        })
        .orElse(ResponseEntity.notFound().build());
  }

  @Override
  public ResponseEntity<Void> deleteTeam(UUID id) {
    if (this.teamService.findTeamById(id).isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    this.teamService.deleteTeam(id);
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

  @Override
  public ResponseEntity<List<TeamPlayerResponse>> getTeamPlayers(UUID teamId) {
    if (this.teamService.findTeamById(teamId).isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(this.teamPlayerMapper.toResponseList(
        this.teamPlayerService.findTeamPlayersByTeamId(teamId)));
  }

  @Override
  public ResponseEntity<TeamPlayerResponse> addPlayerToTeam(
      UUID teamId, TeamPlayerCreateRequest teamPlayerCreateRequest) {
    Team team = this.teamService.findTeamById(teamId).orElse(null);
    Player player = this.playerService.findPlayerById(teamPlayerCreateRequest.getPlayerId())
        .orElse(null);
    if (team == null || player == null) {
      return ResponseEntity.notFound().build();
    }
    if (this.teamPlayerService.existsPlayerInTeam(teamId, player.getId())) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    TeamPlayer teamPlayer = new TeamPlayer();
    teamPlayer.setTeam(team);
    teamPlayer.setPlayer(player);
    teamPlayer.setDorsal(teamPlayerCreateRequest.getDorsal());
    teamPlayer.setStatus(teamPlayerCreateRequest.getStatus() == null
        ? PlayerStatus.PENDING_DOCS
        : PlayerStatus.valueOf(teamPlayerCreateRequest.getStatus().name()));
    TeamPlayer saved = this.teamPlayerService.saveTeamPlayer(teamPlayer);
    return ResponseEntity.status(HttpStatus.CREATED).body(this.teamPlayerMapper.toResponse(saved));
  }

  @Override
  public ResponseEntity<TeamPlayerResponse> updateTeamPlayer(
      UUID teamId, UUID teamPlayerId, TeamPlayerUpdateRequest request) {
    TeamPlayer teamPlayer = this.teamPlayerService.findTeamPlayerById(teamPlayerId).orElse(null);
    if (teamPlayer == null || !teamPlayer.getTeam().getId().equals(teamId)) {
      return ResponseEntity.notFound().build();
    }

    if (Boolean.TRUE.equals(request.getClearDorsal())) {
      teamPlayer.setDorsal(null);
    } else if (request.getDorsal() != null) {
      teamPlayer.setDorsal(request.getDorsal());
    }
    TeamPlayerUpdateRequest.StatusEnum requestedStatus = request.getStatus();
    if (requestedStatus != null) {
      teamPlayer.setStatus(PlayerStatus.valueOf(requestedStatus.name()));
    }
    TeamPlayer saved = this.teamPlayerService.saveTeamPlayer(teamPlayer);
    return ResponseEntity.ok(this.teamPlayerMapper.toResponse(saved));
  }

  @Override
  public ResponseEntity<Void> removePlayerFromTeam(UUID teamId, UUID teamPlayerId) {
    TeamPlayer teamPlayer = this.teamPlayerService.findTeamPlayerById(teamPlayerId).orElse(null);
    if (teamPlayer == null || !teamPlayer.getTeam().getId().equals(teamId)) {
      return ResponseEntity.notFound().build();
    }
    this.teamPlayerService.deleteTeamPlayer(teamPlayerId);
    return ResponseEntity.noContent().build();
  }
}
