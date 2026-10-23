package com.cdoropesa.api.service;

import com.cdoropesa.api.model.Team;
import com.cdoropesa.api.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de lógica de negocio para la gestión de Equipos (Team).
 * Maneja las operaciones de persistencia y reglas de los equipos del CD Oropesa.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Service
public class TeamService {

  private final TeamRepository teamRepository;

  /**
   * Inyección de dependencias por constructor.
   *
   * @param teamRepository Repositorio JPA para la entidad Team.
   */
  public TeamService(TeamRepository teamRepository) {
    this.teamRepository = teamRepository;
  }

  /**
   * Obtiene el listado completo de todos los equipos del club.
   *
   * @return Lista de objetos Team.
   */
  @Transactional(readOnly = true)
  public List<Team> findAllTeams() {
    return this.teamRepository.findAll();
  }

  /**
   * Busca un equipo específico por su identificador único UUID.
   *
   * @param id Identificador único del equipo.
   * @return Un Optional con el equipo si se encuentra.
   */
  @Transactional(readOnly = true)
  public Optional<Team> findTeamById(UUID id) {
    return this.teamRepository.findById(id);
  }

  /**
   * Guarda o actualiza un equipo en el sistema.
   *
   * @param team Objeto Team a persistir.
   * @return La entidad Team guardada.
   */
  @Transactional
  public Team saveTeam(Team team) {
    return this.teamRepository.save(team);
  }

  /**
   * Elimina un equipo por su identificador.
   *
   * @param id Identificador único del equipo a eliminar.
   */
  @Transactional
  public void deleteTeam(UUID id) {
    this.teamRepository.deleteById(id);
  }
}
