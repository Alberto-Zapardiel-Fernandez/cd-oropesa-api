package com.cdoropesa.api.mapper;

import com.cdoropesa.api.dto.CoachRequest;
import com.cdoropesa.api.dto.CoachResponse;
import com.cdoropesa.api.model.Coach;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapeador utilizando MapStruct para transformar bidireccionalmente
 * entre la entidad Coach y sus respectivos DTOs de transferencia.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public interface CoachMapper {

  /**
   * Convierte una entidad Coach en su DTO de respuesta.
   *
   * @param coach Entidad JPA Coach.
   * @return DTO CoachResponse para la API.
   */
  CoachResponse toResponse(Coach coach);

  /**
   * Convierte una lista de entidades Coach en una lista de DTOs de respuesta.
   *
   * @param coaches Lista de entidades Coach.
   * @return Lista de DTOs CoachResponse.
   */
  List<CoachResponse> toResponseList(List<Coach> coaches);

  /**
   * Convierte un DTO de petición CoachRequest en una entidad Coach para su persistencia.
   * Ignora el ID y la fecha de creación para que la BBDD los gestione de forma autónoma.
   *
   * @param request DTO con los datos de entrada de la petición.
   * @return Entidad Coach lista para ser guardada.
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "phone", ignore = true)
  @Mapping(target = "user", ignore = true)
  @Mapping(target = "photoUrl", ignore = true)
  @Mapping(target = "dniFileUrl", ignore = true)
  Coach toEntity(CoachRequest request);
}
