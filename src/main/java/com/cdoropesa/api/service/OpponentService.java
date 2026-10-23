package com.cdoropesa.api.service;

import com.cdoropesa.api.model.Opponent;
import com.cdoropesa.api.repository.OpponentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de lógica de negocio para la gestión de Clubes Rivales (Opponent).
 * Maneja las operaciones de persistencia y consulta del directorio de rivales.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Service
public class OpponentService {

  private final OpponentRepository opponentRepository;

  /**
   * Inyección de dependencias por constructor.
   *
   * @param opponentRepository Repositorio JPA para la entidad Opponent.
   */
  public OpponentService(OpponentRepository opponentRepository) {
    this.opponentRepository = opponentRepository;
  }

  /**
   * Obtiene el listado completo de todos los clubes rivales registrados.
   *
   * @return Lista de objetos Opponent.
   */
  @Transactional(readOnly = true)
  public List<Opponent> findAllOpponents() {
    return this.opponentRepository.findAll();
  }

  /**
   * Busca un club rival específico por su identificador único UUID.
   *
   * @param id Identificador único del rival.
   * @return Un Optional con el rival si se encuentra.
   */
  @Transactional(readOnly = true)
  public Optional<Opponent> findOpponentById(UUID id) {
    return this.opponentRepository.findById(id);
  }

  /**
   * Guarda o actualiza un club rival en el sistema.
   *
   * @param opponent Objeto Opponent a persistir.
   * @return La entidad Opponent guardada.
   */
  @Transactional
  public Opponent saveOpponent(Opponent opponent) {
    return this.opponentRepository.save(opponent);
  }

  /**
   * Elimina un club rival por su identificador.
   *
   * @param id Identificador único del rival a eliminar.
   */
  @Transactional
  public void deleteOpponent(UUID id) {
    this.opponentRepository.deleteById(id);
  }
}
