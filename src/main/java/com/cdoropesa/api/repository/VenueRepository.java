package com.cdoropesa.api.repository;

import com.cdoropesa.api.model.Venue;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de Spring Data JPA para la entidad Venue.
 * Gestiona el acceso a datos para las instalaciones deportivas.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Repository
public interface VenueRepository extends JpaRepository<Venue, UUID> {
  // Se añadirán consultas personalizadas según se requieran.
}
