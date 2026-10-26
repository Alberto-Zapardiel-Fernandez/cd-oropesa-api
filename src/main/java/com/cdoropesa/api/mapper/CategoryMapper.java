package com.cdoropesa.api.mapper;

import com.cdoropesa.api.dto.CategoryRequest;
import com.cdoropesa.api.dto.CategoryResponse;
import com.cdoropesa.api.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapeador utilizando MapStruct para transformar bidireccionalmente
 * entre la entidad Category y sus respectivos DTOs de transferencia.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper {

  /**
   * Convierte una entidad Category en su DTO de respuesta.
   *
   * @param category Entidad JPA Category.
   * @return DTO CategoryResponse para la API.
   */
  CategoryResponse toResponse(Category category);

  /**
   * Convierte una lista de entidades Category en una lista de DTOs de respuesta.
   *
   * @param categories Lista de entidades Category.
   * @return Lista de DTOs CategoryResponse.
   */
  List<CategoryResponse> toResponseList(List<Category> categories);

  /**
   * Convierte un DTO de petición CategoryRequest en una entidad Category para su persistencia.
   * Ignora el ID y la fecha de creación para que la BBDD los gestione de forma autónoma.
   *
   * @param request DTO con los datos de entrada de la petición.
   * @return Entidad Category lista para ser guardada.
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  Category toEntity(CategoryRequest request);
}
