package com.cdoropesa.api.service;

import com.cdoropesa.api.model.TeamPlayer;
import com.cdoropesa.api.repository.TeamPlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de lógica de negocio para la gestión de inscripciones de jugadores en equipos (TeamPlayer).
 * Maneja las operaciones de persistencia y control de fichas federativas.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Service
public class TeamPlayerService {

  private final TeamPlayerRepository teamPlayerRepository;

  /**
   * Inyección de dependencias por constructor.
   *
   * @param teamPlayerRepository Repositorio JPA para la entidad TeamPlayer.
   */
  public TeamPlayerService(TeamPlayerRepository teamPlayerRepository) {
    this.teamPlayerRepository = teamPlayerRepository;
  }

  /**
   * Obtiene el listado completo de todas las inscripciones registradas.
   *
   * @return Lista de objetos TeamPlayer.
   */
  @Transactional(readOnly = true)
  public List<TeamPlayer> findAllTeamPlayers() {
    return this.teamPlayerRepository.findAll();
  }

  /**
   * Busca una inscripción específica por su identificador único UUID.
   *
   * @param id Identificador único de la inscripción.
   * @return Un Optional con la inscripción si se encuentra.
   */
  @Transactional(readOnly = true)
  public Optional<TeamPlayer> findTeamPlayerById(UUID id) {
    return this.teamPlayerRepository.findById(id);
  }

  /**
   * Guarda o actualiza una inscripción en el sistema.
   *
   * @param teamPlayer Objeto TeamPlayer a persistir.
   * @return La entidad TeamPlayer guardada.
   */
  @Transactional
  public TeamPlayer saveTeamPlayer(TeamPlayer teamPlayer) {
    return this.teamPlayerRepository.save(teamPlayer);
  }

  /**
   * Elimina una inscripción por su identificador.
   *
   * @param id Identificador único de la inscripción a eliminar.
   */
  @Transactional
  public void deleteTeamPlayer(UUID id) {
    this.teamPlayerRepository.deleteById(id);
  }
}
