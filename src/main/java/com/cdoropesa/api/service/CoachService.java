package com.cdoropesa.api.service;

import com.cdoropesa.api.model.Coach;
import com.cdoropesa.api.repository.CoachRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de lógica de negocio para la gestión de Entrenadores (Coach).
 * Maneja las operaciones de persistencia y consulta del maestro de entrenadores del club.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Service
public class CoachService {

  private final CoachRepository coachRepository;

  /**
   * Inyección de dependencias por constructor.
   *
   * @param coachRepository Repositorio JPA para la entidad Coach.
   */
  public CoachService(CoachRepository coachRepository) {
    this.coachRepository = coachRepository;
  }

  /**
   * Obtiene el listado completo de todos los entrenadores registrados.
   *
   * @return Lista de objetos Coach.
   */
  @Transactional(readOnly = true)
  public List<Coach> findAllCoaches() {
    return this.coachRepository.findAll();
  }

  /**
   * Busca un entrenador específico por su identificador único UUID.
   *
   * @param id Identificador único del entrenador.
   * @return Un Optional con el entrenador si se encuentra.
   */
  @Transactional(readOnly = true)
  public Optional<Coach> findCoachById(UUID id) {
    return this.coachRepository.findById(id);
  }

  /**
   * Guarda o actualiza un entrenador en el sistema.
   *
   * @param coach Objeto Coach a persistir.
   * @return La entidad Coach guardada.
   */
  @Transactional
  public Coach saveCoach(Coach coach) {
    return this.coachRepository.save(coach);
  }

  /**
   * Elimina un entrenador por su identificador.
   *
   * @param id Identificador único del entrenador a eliminar.
   */
  @Transactional
  public void deleteCoach(UUID id) {
    this.coachRepository.deleteById(id);
  }
}
