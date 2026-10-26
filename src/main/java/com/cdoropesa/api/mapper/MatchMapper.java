package com.cdoropesa.api.mapper;

import com.cdoropesa.api.dto.MatchRequest;
import com.cdoropesa.api.dto.MatchResponse;
import com.cdoropesa.api.model.Match;
import com.cdoropesa.api.model.Opponent;
import com.cdoropesa.api.model.Team;
import com.cdoropesa.api.model.Venue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapeador utilizando MapStruct para transformar bidireccionalmente
 * entre la entidad Match y sus respectivos DTOs de transferencia.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public interface MatchMapper {

  /**
   * Convierte una entidad Match en su DTO de respuesta.
   *
   * @param match Entidad JPA Match.
   * @return DTO MatchResponse para la API.
   */
  MatchResponse toResponse(Match match);

  /**
   * Convierte una lista de entidades Match en una lista de DTOs de respuesta.
   *
   * @param matches Lista de entidades Match.
   * @return Lista de DTOs MatchResponse.
   */
  List<MatchResponse> toResponseList(List<Match> matches);

  /**
   * Convierte un DTO de petición MatchRequest en una entidad Match para su persistencia,
   * enlazando el equipo, el rival y la instalación deportiva correspondiente.
   *
   * @param request DTO con los datos de entrada del partido.
   * @param team Entidad Team resuelta previamente.
   * @param opponent Entidad Opponent resuelta previamente.
   * @param venue Entidad Venue resuelta previamente.
   * @return Entidad Match lista para ser guardada.
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "team", source = "team")
  @Mapping(target = "opponent", source = "opponent")
  @Mapping(target = "venue", source = "venue")
  Match toEntity(MatchRequest request, Team team, Opponent opponent, Venue venue);
}
