package com.cdoropesa.api.controller;

import com.cdoropesa.api.dto.CategoryRequest;
import com.cdoropesa.api.dto.CategoryResponse;
import com.cdoropesa.api.generated.CategoriesApi;
import com.cdoropesa.api.mapper.CategoryMapper;
import com.cdoropesa.api.model.Category;
import com.cdoropesa.api.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador REST para la gestión de Categorías base de competición.
 * Implementa la interfaz contract-first generada automáticamente por OpenAPI (CategoriesApi).
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1")
public class CategoryController implements CategoriesApi {

  private final CategoryService categoryService;
  private final CategoryMapper categoryMapper;

  /**
   * Inyección de dependencias mediante constructor.
   *
   * @param categoryService Servicio de lógica de negocio para categorías.
   * @param categoryMapper Mapeador MapStruct entre entidades y DTOs.
   */
  public CategoryController(CategoryService categoryService, CategoryMapper categoryMapper) {
    this.categoryService = categoryService;
    this.categoryMapper = categoryMapper;
  }

  /**
   * Obtiene el listado completo de todas las categorías registradas.
   *
   * @return ResponseEntity con la lista de categorías en formato DTO y código HTTP 200.
   */
  @Override
  public ResponseEntity<List<CategoryResponse>> getAllCategories() {
    List<Category> categories = this.categoryService.findAllCategories();
    return ResponseEntity.ok(this.categoryMapper.toResponseList(categories));
  }

  /**
   * Registra y crea una nueva categoría en el sistema.
   *
   * @param categoryRequest DTO con los datos de la categoría a crear.
   * @return ResponseEntity con la categoría creada y código HTTP 201.
   */
  @Override
  public ResponseEntity<CategoryResponse> createCategory(CategoryRequest categoryRequest) {
    Category category = this.categoryMapper.toEntity(categoryRequest);
    Category saved = this.categoryService.saveCategory(category);
    return ResponseEntity.status(HttpStatus.CREATED).body(this.categoryMapper.toResponse(saved));
  }
}
