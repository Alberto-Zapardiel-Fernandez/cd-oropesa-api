package com.cdoropesa.api.service;

import com.cdoropesa.api.model.Player;
import com.cdoropesa.api.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de lógica de negocio para la gestión del Maestro de Jugadores (Player).
 * Maneja las operaciones de persistencia y consulta de los deportistas del CD Oropesa.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Service
public class PlayerService {

  private final PlayerRepository playerRepository;

  /**
   * Inyección de dependencias por constructor.
   *
   * @param playerRepository Repositorio JPA para la entidad Player.
   */
  public PlayerService(PlayerRepository playerRepository) {
    this.playerRepository = playerRepository;
  }

  /**
   * Obtiene el listado completo de todos los jugadores registrados en el maestro del club.
   *
   * @return Lista de objetos Player.
   */
  @Transactional(readOnly = true)
  public List<Player> findAllPlayers() {
    return this.playerRepository.findAll();
  }

  /**
   * Busca un jugador específico por su identificador único UUID.
   *
   * @param id Identificador único del jugador.
   * @return Un Optional con el jugador si se encuentra.
   */
  @Transactional(readOnly = true)
  public Optional<Player> findPlayerById(UUID id) {
    return this.playerRepository.findById(id);
  }

  /**
   * Guarda o actualiza la ficha de un jugador en el sistema.
   *
   * @param player Objeto Player a persistir.
   * @return La entidad Player guardada.
   */
  @Transactional
  public Player savePlayer(Player player) {
    return this.playerRepository.save(player);
  }

  /**
   * Elimina la ficha de un jugador del maestro por su identificador.
   *
   * @param id Identificador único del jugador a eliminar.
   */
  @Transactional
  public void deletePlayer(UUID id) {
    this.playerRepository.deleteById(id);
  }
}
