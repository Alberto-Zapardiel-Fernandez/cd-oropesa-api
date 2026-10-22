package com.cdoropesa.api.repository;

import com.cdoropesa.api.model.Season;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de Spring Data JPA para la entidad Season.
 * Gestiona las operaciones de persistencia y acceso a datos para las temporadas del club.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Repository
public interface SeasonRepository extends JpaRepository<Season, UUID> {
  // Se pueden añadir métodos de consulta personalizados (Query Methods) en este punto si se requiere.
}
