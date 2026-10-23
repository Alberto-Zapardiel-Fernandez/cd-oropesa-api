package com.cdoropesa.api.repository;

import com.cdoropesa.api.model.Standing;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de Spring Data JPA para la entidad Standing.
 * Gestiona el acceso a datos para las tablas de clasificación de las competiciones.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Repository
public interface StandingRepository extends JpaRepository<Standing, UUID> {
  // Se añadirán consultas personalizadas según se requieran.
}
