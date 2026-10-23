package com.cdoropesa.api.repository;

import com.cdoropesa.api.model.Team;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de Spring Data JPA para la entidad Team.
 * Gestiona el acceso a datos para los equipos del club.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Repository
public interface TeamRepository extends JpaRepository<Team, UUID> {
  // Se añadirán consultas personalizadas más adelante (por ejemplo, buscar equipos por temporada).
}
