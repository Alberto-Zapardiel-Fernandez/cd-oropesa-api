package com.cdoropesa.api.service;

import com.cdoropesa.api.model.Category;
import com.cdoropesa.api.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de lógica de negocio para la gestión de Categorías (Category).
 * Maneja las operaciones de consulta y persistencia para las categorías base del club CD Oropesa.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Service
public class CategoryService {

  private final CategoryRepository categoryRepository;

  /**
   * Inyección de dependencias por constructor.
   *
   * @param categoryRepository Repositorio JPA para la entidad Category.
   */
  public CategoryService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  /**
   * Obtiene el listado completo de todas las categorías registradas.
   *
   * @return Lista de objetos Category.
   */
  @Transactional(readOnly = true)
  public List<Category> findAllCategories() {
    return this.categoryRepository.findAll();
  }

  /**
   * Busca una categoría específica por su identificador único UUID.
   *
   * @param id Identificador único de la categoría.
   * @return Un Optional que contiene la categoría si existe.
   */
  @Transactional(readOnly = true)
  public Optional<Category> findCategoryById(UUID id) {
    return this.categoryRepository.findById(id);
  }

  /**
   * Guarda o actualiza una categoría en el sistema.
   *
   * @param category Objeto Category a persistir.
   * @return La entidad Category guardada.
   */
  @Transactional
  public Category saveCategory(Category category) {
    return this.categoryRepository.save(category);
  }

  /**
   * Elimina una categoría por su identificador.
   *
   * @param id Identificador único de la categoría a eliminar.
   */
  @Transactional
  public void deleteCategory(UUID id) {
    this.categoryRepository.deleteById(id);
  }
}
