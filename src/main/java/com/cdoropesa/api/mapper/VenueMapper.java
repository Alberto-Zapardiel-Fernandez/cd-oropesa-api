package com.cdoropesa.api.mapper;

import com.cdoropesa.api.dto.VenueRequest;
import com.cdoropesa.api.dto.VenueResponse;
import com.cdoropesa.api.model.Venue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapeador utilizando MapStruct para transformar bidireccionalmente
 * entre la entidad Venue y sus respectivos DTOs de transferencia.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public interface VenueMapper {

  /**
   * Convierte una entidad Venue en su DTO de respuesta.
   *
   * @param venue Entidad JPA Venue.
   * @return DTO VenueResponse para la API.
   */
  VenueResponse toResponse(Venue venue);

  /**
   * Convierte una lista de entidades Venue en una lista de DTOs de respuesta.
   *
   * @param venues Lista de entidades Venue.
   * @return Lista de DTOs VenueResponse.
   */
  List<VenueResponse> toResponseList(List<Venue> venues);

  /**
   * Convierte un DTO de petición VenueRequest en una entidad Venue para su persistencia.
   * Ignora el ID y la fecha de creación para que la BBDD los gestione de forma autónoma.
   *
   * @param request DTO con los datos de entrada de la petición.
   * @return Entidad Venue lista para ser guardada.
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  Venue toEntity(VenueRequest request);
}
