package com.cdoropesa.api.repository;

import com.cdoropesa.api.model.Category;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de Spring Data JPA para la entidad Category.
 * Gestiona las operaciones de persistencia y acceso a datos para las categorías base del club.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
  // Se añadirán métodos de consulta personalizados más adelante según la lógica de negocio.
}
