package com.cdoropesa.api.repository;

import com.cdoropesa.api.model.Player;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de Spring Data JPA para la entidad Player.
 * Gestiona el acceso a datos para el maestro de jugadores del club.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Repository
public interface PlayerRepository extends JpaRepository<Player, UUID> {
  // Se añadirán métodos de consulta personalizados más adelante (ej: findByDni, findByLastNameContainingIgnoreCase).
}
