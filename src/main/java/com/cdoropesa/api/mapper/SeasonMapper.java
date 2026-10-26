package com.cdoropesa.api.mapper;

import com.cdoropesa.api.dto.SeasonRequest;
import com.cdoropesa.api.dto.SeasonResponse;
import com.cdoropesa.api.model.Season;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapeador utilizando MapStruct para transformar bidireccionalmente
 * entre la entidad Season y sus respectivos DTOs de transferencia.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public interface SeasonMapper {

  /**
   * Convierte una entidad Season en su DTO de respuesta.
   *
   * @param season Entidad JPA Season.
   * @return DTO SeasonResponse para la API.
   */
  SeasonResponse toResponse(Season season);

  /**
   * Convierte una lista de entidades Season en una lista de DTOs de respuesta.
   *
   * @param seasons Lista de entidades Season.
   * @return Lista de DTOs SeasonResponse.
   */
  List<SeasonResponse> toResponseList(List<Season> seasons);

  /**
   * Convierte un DTO de petición SeasonRequest en una entidad Season para su persistencia.
   * Ignora el ID y la fecha de creación para que la BBDD los gestione de forma autónoma.
   *
   * @param request DTO con los datos de entrada de la petición.
   * @return Entidad Season lista para ser guardada.
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  Season toEntity(SeasonRequest request);
}
