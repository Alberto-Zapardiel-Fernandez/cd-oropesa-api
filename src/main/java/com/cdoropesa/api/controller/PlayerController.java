package com.cdoropesa.api.controller;

import com.cdoropesa.api.dto.PlayerRequest;
import com.cdoropesa.api.dto.PlayerResponse;
import com.cdoropesa.api.generated.PlayersApi;
import com.cdoropesa.api.mapper.PlayerMapper;
import com.cdoropesa.api.model.Player;
import com.cdoropesa.api.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * Controlador REST para la gestión del maestro de Jugadores y sus fichas.
 * Implementa la interfaz contract-first generada automáticamente por OpenAPI (PlayersApi).
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1")
public class PlayerController implements PlayersApi {

  private final PlayerService playerService;
  private final PlayerMapper playerMapper;

  /**
   * Inyección de dependencias mediante constructor.
   *
   * @param playerService Servicio de lógica de negocio para jugadores.
   * @param playerMapper Mapeador MapStruct entre entidades y DTOs.
   */
  public PlayerController(PlayerService playerService, PlayerMapper playerMapper) {
    this.playerService = playerService;
    this.playerMapper = playerMapper;
  }

  /**
   * Obtiene el listado completo de todos los jugadores registrados en el club.
   *
   * @return ResponseEntity con la lista de jugadores en formato DTO y código HTTP 200.
   */
  @Override
  public ResponseEntity<List<PlayerResponse>> getAllPlayers() {
    List<Player> players = this.playerService.findAllPlayers();
    return ResponseEntity.ok(this.playerMapper.toResponseList(players));
  }

  /**
   * Registra y crea un nuevo jugador en el sistema.
   *
   * @param playerRequest DTO con los datos del jugador a registrar.
   * @return ResponseEntity con el jugador creado y código HTTP 201.
   */
  @Override
  public ResponseEntity<PlayerResponse> createPlayer(PlayerRequest playerRequest) {
    Player player = this.playerMapper.toEntity(playerRequest);
    Player saved = this.playerService.savePlayer(player);
    return ResponseEntity.status(HttpStatus.CREATED).body(this.playerMapper.toResponse(saved));
  }

  /**
   * Busca y obtiene el detalle de un jugador específico a partir de su identificador UUID.
   *
   * @param id Identificador único del jugador.
   * @return ResponseEntity con el jugador encontrado (200) o un 404 si no existe.
   */
  @Override
  public ResponseEntity<PlayerResponse> getPlayerById(UUID id) {
    return this.playerService.findPlayerById(id)
        .map(player -> ResponseEntity.ok(this.playerMapper.toResponse(player)))
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * Actualiza los datos de un jugador existente.
   *
   * @param id Identificador único del jugador a actualizar.
   * @param playerRequest DTO con los nuevos datos.
   * @return ResponseEntity con el jugador actualizado (200) o 404 si no se encuentra.
   */
  @Override
  public ResponseEntity<PlayerResponse> updatePlayer(UUID id, PlayerRequest playerRequest) {
    return this.playerService.findPlayerById(id)
        .map(existingPlayer -> {
          Player updatedPlayer = this.playerMapper.toEntity(playerRequest);
          updatedPlayer.setId(existingPlayer.getId());
          updatedPlayer.setCreatedAt(existingPlayer.getCreatedAt());
          Player saved = this.playerService.savePlayer(updatedPlayer);
          return ResponseEntity.ok(this.playerMapper.toResponse(saved));
        })
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * Elimina o da de baja a un jugador del sistema por su identificador UUID.
   *
   * @param id Identificador único del jugador a eliminar.
   * @return ResponseEntity con código 204 si se elimina con éxito o 404 si no se encuentra.
   */
  @Override
  public ResponseEntity<Void> deletePlayer(UUID id) {
    if (this.playerService.findPlayerById(id).isPresent()) {
      this.playerService.deletePlayer(id);
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
  }
}
