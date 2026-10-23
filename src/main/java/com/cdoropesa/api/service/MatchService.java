package com.cdoropesa.api.service;

import com.cdoropesa.api.model.Match;
import com.cdoropesa.api.repository.MatchRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de lógica de negocio para la gestión de Partidos (Match).
 * Maneja las operaciones de planificación de partidos y actualización de resultados.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Service
public class MatchService {

  private final MatchRepository matchRepository;

  /**
   * Inyección de dependencias por constructor.
   *
   * @param matchRepository Repositorio JPA para la entidad Match.
   */
  public MatchService(MatchRepository matchRepository) {
    this.matchRepository = matchRepository;
  }

  /**
   * Obtiene el listado completo de todos los partidos registrados.
   *
   * @return Lista de objetos Match.
   */
  @Transactional(readOnly = true)
  public List<Match> findAllMatches() {
    return this.matchRepository.findAll();
  }

  /**
   * Busca un partido específico por su identificador único UUID.
   *
   * @param id Identificador único del partido.
   * @return Un Optional con el partido si se encuentra.
   */
  @Transactional(readOnly = true)
  public Optional<Match> findMatchById(UUID id) {
    return this.matchRepository.findById(id);
  }

  /**
   * Guarda o actualiza un partido en el sistema.
   *
   * @param match Objeto Match a persistir.
   * @return La entidad Match guardada.
   */
  @Transactional
  public Match saveMatch(Match match) {
    return this.matchRepository.save(match);
  }

  /**
   * Elimina un partido por su identificador.
   *
   * @param id Identificador único del partido a eliminar.
   */
  @Transactional
  public void deleteMatch(UUID id) {
    this.matchRepository.deleteById(id);
  }
}
