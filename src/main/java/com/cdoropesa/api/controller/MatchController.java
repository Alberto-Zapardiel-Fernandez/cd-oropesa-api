package com.cdoropesa.api.controller;

import com.cdoropesa.api.dto.MatchRequest;
import com.cdoropesa.api.dto.MatchResponse;
import com.cdoropesa.api.generated.MatchesApi;
import com.cdoropesa.api.mapper.MatchMapper;
import com.cdoropesa.api.model.Match;
import com.cdoropesa.api.model.Opponent;
import com.cdoropesa.api.model.Team;
import com.cdoropesa.api.model.Venue;
import com.cdoropesa.api.service.MatchService;
import com.cdoropesa.api.service.OpponentService;
import com.cdoropesa.api.service.TeamService;
import com.cdoropesa.api.service.VenueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * Controlador REST para la gestión del calendario de partidos y sus resultados.
 * Implementa la interfaz contract-first generada automáticamente por OpenAPI (MatchesApi).
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1")
public class MatchController implements MatchesApi {

  private final MatchService matchService;
  private final TeamService teamService;
  private final OpponentService opponentService;
  private final VenueService venueService;
  private final MatchMapper matchMapper;

  /**
   * Inyección de dependencias mediante constructor.
   *
   * @param matchService Servicio de lógica de negocio para partidos.
   * @param teamService Servicio de equipos para resolver la relación.
   * @param opponentService Servicio de rivales para resolver la relación.
   * @param venueService Servicio de instalaciones para resolver la relación.
   * @param matchMapper Mapeador MapStruct entre entidades y DTOs.
   */
  public MatchController(MatchService matchService,
                         TeamService teamService,
                         OpponentService opponentService,
                         VenueService venueService,
                         MatchMapper matchMapper) {
    this.matchService = matchService;
    this.teamService = teamService;
    this.opponentService = opponentService;
    this.venueService = venueService;
    this.matchMapper = matchMapper;
  }

  /**
   * Obtiene el listado completo de partidos programados y jugados.
   *
   * @return ResponseEntity con la lista de partidos en formato DTO y código HTTP 200.
   */
  @Override
  public ResponseEntity<List<MatchResponse>> getAllMatches() {
    List<Match> matches = this.matchService.findAllMatches();
    return ResponseEntity.ok(this.matchMapper.toResponseList(matches));
  }

  /**
   * Programa y registra un nuevo partido en el calendario.
   *
   * @param matchRequest DTO con los datos del partido a crear.
   * @return ResponseEntity con el partido creado y código HTTP 201, o 400 si faltan relaciones.
   */
  @Override
  public ResponseEntity<MatchResponse> createMatch(MatchRequest matchRequest) {
    Team team = this.teamService.findTeamById(matchRequest.getTeamId()).orElse(null);
    Opponent opponent = this.opponentService.findOpponentById(matchRequest.getOpponentId()).orElse(null);
    Venue venue = this.venueService.findVenueById(matchRequest.getVenueId()).orElse(null);

    if (team == null || opponent == null || venue == null) {
      return ResponseEntity.badRequest().build();
    }

    Match match = this.matchMapper.toEntity(matchRequest, team, opponent, venue);
    Match saved = this.matchService.saveMatch(match);
    return ResponseEntity.status(HttpStatus.CREATED).body(this.matchMapper.toResponse(saved));
  }

  @Override
  public ResponseEntity<MatchResponse> getMatchById(UUID id) {
    return this.matchService.findMatchById(id)
        .map(match -> ResponseEntity.ok(this.matchMapper.toResponse(match)))
        .orElse(ResponseEntity.notFound().build());
  }

  @Override
  public ResponseEntity<MatchResponse> updateMatch(UUID id, MatchRequest matchRequest) {
    return this.matchService.findMatchById(id)
        .map(existingMatch -> {
          Team team = this.teamService.findTeamById(matchRequest.getTeamId()).orElse(null);
          Opponent opponent = this.opponentService
              .findOpponentById(matchRequest.getOpponentId()).orElse(null);
          Venue venue = this.venueService.findVenueById(matchRequest.getVenueId()).orElse(null);
          if (team == null || opponent == null || venue == null) {
            return ResponseEntity.badRequest().<MatchResponse>build();
          }

          Match updatedMatch = this.matchMapper.toEntity(matchRequest, team, opponent, venue);
          updatedMatch.setId(existingMatch.getId());
          updatedMatch.setCreatedAt(existingMatch.getCreatedAt());
          Match saved = this.matchService.saveMatch(updatedMatch);
          return ResponseEntity.ok(this.matchMapper.toResponse(saved));
        })
        .orElse(ResponseEntity.notFound().build());
  }

  @Override
  public ResponseEntity<Void> deleteMatch(UUID id) {
    if (this.matchService.findMatchById(id).isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    this.matchService.deleteMatch(id);
    return ResponseEntity.noContent().build();
  }
}
