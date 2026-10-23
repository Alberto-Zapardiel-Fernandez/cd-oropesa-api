package com.cdoropesa.api.repository;

import com.cdoropesa.api.model.TeamStaff;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de Spring Data JPA para la entidad TeamStaff.
 * Gestiona el acceso a datos para las asignaciones del cuerpo técnico a los equipos.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Repository
public interface TeamStaffRepository extends JpaRepository<TeamStaff, UUID> {
  // Se añadirán consultas personalizadas según se requieran.
}
