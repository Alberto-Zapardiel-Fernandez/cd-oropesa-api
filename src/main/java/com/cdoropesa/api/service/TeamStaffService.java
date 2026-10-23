package com.cdoropesa.api.service;

import com.cdoropesa.api.model.TeamStaff;
import com.cdoropesa.api.repository.TeamStaffRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de lógica de negocio para la gestión del Cuerpo Técnico en equipos (TeamStaff).
 * Maneja las operaciones de persistencia y asignación de técnicos a los equipos del club.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Service
public class TeamStaffService {

  private final TeamStaffRepository teamStaffRepository;

  /**
   * Inyección de dependencias por constructor.
   *
   * @param teamStaffRepository Repositorio JPA para la entidad TeamStaff.
   */
  public TeamStaffService(TeamStaffRepository teamStaffRepository) {
    this.teamStaffRepository = teamStaffRepository;
  }

  /**
   * Obtiene el listado completo de todas las asignaciones de cuerpo técnico.
   *
   * @return Lista de objetos TeamStaff.
   */
  @Transactional(readOnly = true)
  public List<TeamStaff> findAllTeamStaff() {
    return this.teamStaffRepository.findAll();
  }

  /**
   * Busca una asignación de cuerpo técnico por su identificador único UUID.
   *
   * @param id Identificador único de la asignación.
   * @return Un Optional con el registro si se encuentra.
   */
  @Transactional(readOnly = true)
  public Optional<TeamStaff> findTeamStaffById(UUID id) {
    return this.teamStaffRepository.findById(id);
  }

  /**
   * Guarda o actualiza una asignación de cuerpo técnico en el sistema.
   *
   * @param teamStaff Objeto TeamStaff a persistir.
   * @return La entidad TeamStaff guardada.
   */
  @Transactional
  public TeamStaff saveTeamStaff(TeamStaff teamStaff) {
    return this.teamStaffRepository.save(teamStaff);
  }

  /**
   * Elimina una asignación de cuerpo técnico por su identificador.
   *
   * @param id Identificador único del registro a eliminar.
   */
  @Transactional
  public void deleteTeamStaff(UUID id) {
    this.teamStaffRepository.deleteById(id);
  }
}