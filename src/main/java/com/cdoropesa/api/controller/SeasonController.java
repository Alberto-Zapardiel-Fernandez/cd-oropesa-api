package com.cdoropesa.api.controller;

import com.cdoropesa.api.dto.SeasonRequest;
import com.cdoropesa.api.dto.SeasonResponse;
import com.cdoropesa.api.generated.SeasonsApi;
import com.cdoropesa.api.mapper.SeasonMapper;
import com.cdoropesa.api.model.Season;
import com.cdoropesa.api.service.SeasonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * Controlador REST para la gestión de Temporadas.
 * Implementa la interfaz contract-first generada automáticamente por OpenAPI (SeasonsApi).
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1")
public class SeasonController implements SeasonsApi {

  private final SeasonService seasonService;
  private final SeasonMapper seasonMapper;

  /**
   * Inyección de dependencias mediante constructor.
   *
   * @param seasonService Servicio de lógica de negocio para temporadas.
   * @param seasonMapper Mapeador MapStruct entre entidades y DTOs.
   */
  public SeasonController(SeasonService seasonService, SeasonMapper seasonMapper) {
    this.seasonService = seasonService;
    this.seasonMapper = seasonMapper;
  }

  /**
   * Obtiene el listado completo de todas las temporadas registradas.
   *
   * @return ResponseEntity con la lista de temporadas en formato DTO y código HTTP 200.
   */
  @Override
  public ResponseEntity<List<SeasonResponse>> getAllSeasons() {
    List<Season> seasons = this.seasonService.findAllSeasons();
    return ResponseEntity.ok(this.seasonMapper.toResponseList(seasons));
  }

  /**
   * Registra y crea una nueva temporada en el sistema.
   *
   * @param seasonRequest DTO con los datos de la temporada a crear.
   * @return ResponseEntity con la temporada creada y código HTTP 201.
   */
  @Override
  public ResponseEntity<SeasonResponse> createSeason(SeasonRequest seasonRequest) {
    Season season = this.seasonMapper.toEntity(seasonRequest);
    Season saved = this.seasonService.saveSeason(season);
    return ResponseEntity.status(HttpStatus.CREATED).body(this.seasonMapper.toResponse(saved));
  }

  /**
   * Busca y obtiene una temporada específica a partir de su identificador UUID.
   *
   * @param id Identificador único de la temporada.
   * @return ResponseEntity con la temporada encontrada (200) o un 404 si no existe.
   */
  @Override
  public ResponseEntity<SeasonResponse> getSeasonById(UUID id) {
    return this.seasonService.findSeasonById(id)
        .map(season -> ResponseEntity.ok(this.seasonMapper.toResponse(season)))
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * Elimina una temporada del sistema por su identificador UUID.
   *
   * @param id Identificador único de la temporada a eliminar.
   * @return ResponseEntity con código 204 si se elimina con éxito o 404 si no se encuentra.
   */
  @Override
  public ResponseEntity<Void> deleteSeason(UUID id) {
    if (this.seasonService.findSeasonById(id).isPresent()) {
      this.seasonService.deleteSeason(id);
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
  }
}
