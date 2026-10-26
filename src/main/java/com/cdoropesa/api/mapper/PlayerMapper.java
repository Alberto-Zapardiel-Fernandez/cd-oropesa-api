package com.cdoropesa.api.mapper;

import com.cdoropesa.api.dto.PlayerRequest;
import com.cdoropesa.api.dto.PlayerResponse;
import com.cdoropesa.api.model.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapeador utilizando MapStruct para transformar bidireccionalmente
 * entre la entidad Player y sus respectivos DTOs de transferencia.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public interface PlayerMapper {

  /**
   * Convierte una entidad Player en su DTO de respuesta.
   *
   * @param player Entidad JPA Player.
   * @return DTO PlayerResponse para la API.
   */
  PlayerResponse toResponse(Player player);

  /**
   * Convierte una lista de entidades Player en una lista de DTOs de respuesta.
   *
   * @param players Lista de entidades Player.
   * @return Lista de DTOs PlayerResponse.
   */
  List<PlayerResponse> toResponseList(List<Player> players);

  /**
   * Convierte un DTO de petición PlayerRequest en una entidad Player para su persistencia.
   * Ignora el ID y la fecha de creación para que la BBDD los gestione de forma autónoma.
   *
   * @param request DTO con los datos de entrada de la petición.
   * @return Entidad Player lista para ser guardada.
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  Player toEntity(PlayerRequest request);
}
