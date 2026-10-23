package com.cdoropesa.api.model;

import com.cdoropesa.api.model.enums.PlayerStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

/**
 * Entidad JPA que representa la tabla 'team_players' en la base de datos PostgreSQL.
 * Gestiona la relación de inscripción de un jugador en un equipo determinado para una temporada,
 * incluyendo su dorsal y el estado administrativo de su ficha.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Entity
@Table(name = "team_players", uniqueConstraints = {
    @UniqueConstraint(name = "unique_player_per_team", columnNames = {"team_id", "player_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamPlayer {

  /**
   * Identificador único de la inscripción (UUID v4).
   */
  @Id
  @GeneratedValue
  @UuidGenerator
  private UUID id;

  /**
   * Equipo del club donde se inscribe el jugador.
   */
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "team_id", nullable = false)
  private Team team;

  /**
   * Jugador inscrito en el equipo.
   */
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "player_id", nullable = false)
  private Player player;

  /**
   * Dorsal asignado al jugador en este equipo.
   */
  @Column(name = "dorsal")
  private Integer dorsal;

  /**
   * Estado administrativo de la ficha del jugador en el equipo (PENDING_DOCS, ACTIVE, etc.).
   */
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PlayerStatus status = PlayerStatus.PENDING_DOCS;

  /**
   * Marca temporal inmutable de la creación del registro.
   */
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt = LocalDateTime.now();
}
