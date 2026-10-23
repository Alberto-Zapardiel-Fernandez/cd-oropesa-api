package com.cdoropesa.api.repository;

import com.cdoropesa.api.model.Match;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de Spring Data JPA para la entidad Match.
 * Gestiona el acceso a datos para el calendario y resultados de partidos.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Repository
public interface MatchRepository extends JpaRepository<Match, UUID> {
  // Se añadirán consultas personalizadas (ej. buscar partidos por equipo o próxima jornada).
}
