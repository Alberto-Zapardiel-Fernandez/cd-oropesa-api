package com.cdoropesa.api.repository;

import com.cdoropesa.api.model.Coach;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de Spring Data JPA para la entidad Coach.
 * Gestiona el acceso a datos para el maestro de entrenadores del club.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Repository
public interface CoachRepository extends JpaRepository<Coach, UUID> {
  // Se añadirán consultas personalizadas según se requieran.
}
