package com.cdoropesa.api.model;

import com.cdoropesa.api.model.enums.MatchStatus;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

/**
 * Entidad JPA que representa la tabla 'matches' en la base de datos PostgreSQL.
 * Almacena la planificación de los encuentros oficiales y amistosos, vinculando al equipo del club,
 * el club rival, la instalación deportiva, los horarios, el marcador y el estado del partido.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Entity
@Table(name = "matches")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Match {

  /**
   * Identificador único del partido (UUID v4).
   */
  @Id
  @GeneratedValue
  @UuidGenerator
  private UUID id;

  /**
   * Equipo del CD Oropesa que disputa el encuentro.
   */
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "team_id", nullable = false)
  private Team team;

  /**
   * Club rival contra el que se compite.
   */
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "opponent_id", nullable = false)
  private Opponent opponent;

  /**
   * Sede o instalación donde se celebra el partido.
   */
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "venue_id", nullable = false)
  private Venue venue;

  /**
   * Número de jornada de liga.
   */
  @Column(nullable = false)
  private Integer matchday;

  /**
   * Fecha oficial en la que se disputa el encuentro.
   */
  @Column(name = "match_date", nullable = false)
  private LocalDate matchDate;

  /**
   * Hora programada para el inicio del partido.
   */
  @Column(name = "match_time")
  private LocalTime matchTime;

  /**
   * Goles anotados por el equipo local.
   */
  @Column(name = "home_score")
  private Integer homeScore;

  /**
   * Goles anotados por el equipo visitante.
   */
  @Column(name = "away_score")
  private Integer awayScore;

  /**
   * Estado actual del partido (SCHEDULED, COMPLETED, SUSPENDED, POSTPONED).
   */
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private MatchStatus status = MatchStatus.SCHEDULED;

  /**
   * Marca temporal inmutable de la creación del registro.
   */
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt = LocalDateTime.now();
}
