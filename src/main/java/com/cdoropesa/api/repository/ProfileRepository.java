package com.cdoropesa.api.repository;

import com.cdoropesa.api.model.Profile;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de Spring Data JPA para la entidad Profile.
 * Gestiona el acceso a datos para los perfiles de usuario del sistema.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Repository
public interface ProfileRepository extends JpaRepository<Profile, UUID> {
  // Se añadirán consultas personalizadas más adelante (ej. buscar por email).
}
