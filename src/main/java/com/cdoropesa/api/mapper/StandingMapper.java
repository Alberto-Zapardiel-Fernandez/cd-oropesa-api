package com.cdoropesa.api.mapper;

import com.cdoropesa.api.dto.StandingResponse;
import com.cdoropesa.api.model.Standing;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapeador utilizando MapStruct para transformar bidireccionalmente
 * entre la entidad Standing y sus respectivos DTOs de transferencia.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public interface StandingMapper {

  /**
   * Convierte una entidad Standing en su DTO de respuesta.
   * Extrae el nombre del rival a partir de la relación con Opponent.
   *
   * @param standing Entidad JPA Standing.
   * @return DTO StandingResponse para la API.
   */
  @Mapping(target = "opponentName", source = "opponentName")
  StandingResponse toResponse(Standing standing);

  /**
   * Convierte una lista de entidades Standing en una lista de DTOs de respuesta.
   *
   * @param standings Lista de entidades Standing.
   * @return Lista de DTOs StandingResponse.
   */
  List<StandingResponse> toResponseList(List<Standing> standings);
}
