package com.cdoropesa.api.service;

import com.cdoropesa.api.model.Standing;
import com.cdoropesa.api.repository.StandingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de lógica de negocio para la gestión de Clasificaciones (Standing).
 * Maneja las operaciones de persistencia y actualización de las tablas de clasificación.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Service
public class StandingService {

  private final StandingRepository standingRepository;

  /**
   * Inyección de dependencias por constructor.
   *
   * @param standingRepository Repositorio JPA para la entidad Standing.
   */
  public StandingService(StandingRepository standingRepository) {
    this.standingRepository = standingRepository;
  }

  /**
   * Obtiene el listado completo de todas las líneas de clasificación registradas.
   *
   * @return Lista de objetos Standing.
   */
  @Transactional(readOnly = true)
  public List<Standing> findAllStandings() {
    return this.standingRepository.findAll();
  }

  /**
   * Busca un registro de clasificación específico por su identificador único UUID.
   *
   * @param id Identificador único del registro.
   * @return Un Optional con el registro si se encuentra.
   */
  @Transactional(readOnly = true)
  public Optional<Standing> findStandingById(UUID id) {
    return this.standingRepository.findById(id);
  }

  /**
   * Guarda o actualiza un registro de clasificación en el sistema.
   *
   * @param standing Objeto Standing a persistir.
   * @return La entidad Standing guardada.
   */
  @Transactional
  public Standing saveStanding(Standing standing) {
    return this.standingRepository.save(standing);
  }

  /**
   * Elimina un registro de clasificación por su identificador.
   *
   * @param id Identificador único del registro a eliminar.
   */
  @Transactional
  public void deleteStanding(UUID id) {
    this.standingRepository.deleteById(id);
  }
}
