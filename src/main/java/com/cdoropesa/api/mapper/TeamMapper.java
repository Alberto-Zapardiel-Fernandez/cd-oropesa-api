package com.cdoropesa.api.mapper;

import com.cdoropesa.api.dto.TeamRequest;
import com.cdoropesa.api.dto.TeamResponse;
import com.cdoropesa.api.model.Category;
import com.cdoropesa.api.model.Season;
import com.cdoropesa.api.model.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapeador utilizando MapStruct para transformar bidireccionalmente
 * entre la entidad Team y sus respectivos DTOs de transferencia.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Mapper(componentModel = "spring", uses = {CategoryMapper.class, SeasonMapper.class})
public interface TeamMapper {

  /**
   * Convierte una entidad Team en su DTO de respuesta.
   *
   * @param team Entidad JPA Team.
   * @return DTO TeamResponse para la API.
   */
  TeamResponse toResponse(Team team);

  /**
   * Convierte una lista de entidades Team en una lista de DTOs de respuesta.
   *
   * @param teams Lista de entidades Team.
   * @return Lista de DTOs TeamResponse.
   */
  List<TeamResponse> toResponseList(List<Team> teams);

  /**
   * Convierte un DTO de petición TeamRequest en una entidad Team para su persistencia,
   * enlazando las entidades de Category y Season mediante sus IDs.
   *
   * @param request DTO con los datos de entrada de la petición.
   * @param category Entidad Category resuelta previamente.
   * @param season Entidad Season resuelta previamente.
   * @return Entidad Team lista para ser guardada.
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "name", source = "request.name")
  @Mapping(target = "category", source = "category")
  @Mapping(target = "season", source = "season")
  Team toEntity(TeamRequest request, Category category, Season season);
}
