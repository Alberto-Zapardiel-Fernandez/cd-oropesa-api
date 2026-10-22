package com.cdoropesa.api.service;

import com.cdoropesa.api.model.Season;
import com.cdoropesa.api.repository.SeasonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de lógica de negocio para la gestión de Temporadas (Seasons).
 * Maneja las operaciones de consulta y persistencia asegurando
 * la integridad de las reglas del club CD Oropesa.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Service
public class SeasonService {

  private final SeasonRepository seasonRepository;

  /**
   * Inyección de dependencias por constructor (buena práctica recomendada frente a @Autowired).
   *
   * @param seasonRepository Repositorio JPA para la entidad Season.
   */
  public SeasonService(SeasonRepository seasonRepository) {
    this.seasonRepository = seasonRepository;
  }

  /**
   * Obtiene el listado completo de todas las temporadas registradas en el sistema.
   *
   * @return Lista de objetos Season.
   */
  @Transactional(readOnly = true)
  public List<Season> findAllSeasons() {
    return this.seasonRepository.findAll();
  }

  /**
   * Busca una temporada específica a partir de su identificador único UUID.
   *
   * @param id Identificador único de la temporada.
   * @return Un Optional que contiene la temporada si existe, o vacío en caso contrario.
   */
  @Transactional(readOnly = true)
  public Optional<Season> findSeasonById(UUID id) {
    return this.seasonRepository.findById(id);
  }

  /**
   * Guarda o actualiza una temporada en la base de datos.
   *
   * @param season Objeto Season con los datos a persistir.
   * @return La entidad Season guardada, incluyendo su ID generado.
   */
  @Transactional
  public Season saveSeason(Season season) {
    return this.seasonRepository.save(season);
  }

  /**
   * Elimina una temporada del sistema por su identificador.
   *
   * @param id Identificador único de la temporada a eliminar.
   */
  @Transactional
  public void deleteSeason(UUID id) {
    this.seasonRepository.deleteById(id);
  }
}
