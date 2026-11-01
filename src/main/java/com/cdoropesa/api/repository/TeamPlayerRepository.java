package com.cdoropesa.api.repository;

import com.cdoropesa.api.model.TeamPlayer;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de Spring Data JPA para la entidad TeamPlayer.
 * Gestiona el acceso a datos para las inscripciones de jugadores en equipos.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Repository
public interface TeamPlayerRepository extends JpaRepository<TeamPlayer, UUID> {
  List<TeamPlayer> findAllByTeam_Id(UUID teamId);

  boolean existsByTeam_IdAndPlayer_Id(UUID teamId, UUID playerId);
}
