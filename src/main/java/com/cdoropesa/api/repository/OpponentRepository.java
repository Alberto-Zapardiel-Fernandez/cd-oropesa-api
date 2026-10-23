package com.cdoropesa.api.repository;

import com.cdoropesa.api.model.Opponent;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de Spring Data JPA para la entidad Opponent.
 * Gestiona el acceso a datos para el directorio de clubes rivales.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Repository
public interface OpponentRepository extends JpaRepository<Opponent, UUID> {
  // Se añadirán consultas personalizadas según se requieran.
}
