package com.cdoropesa.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

/**
 * Entidad JPA que representa la tabla 'standings' en la base de datos PostgreSQL.
 * Almacena las estadísticas de clasificación de los equipos y sus rivales en las diferentes competiciones,
 * registrando partidos jugados, ganados, empatados, perdidos, goles y puntos totales.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Entity
@Table(name = "standings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Standing {

  /**
   * Identificador único del registro de clasificación (UUID v4).
   */
  @Id
  @GeneratedValue
  @UuidGenerator
  private UUID id;

  /**
   * Equipo del club al que pertenece esta tabla de clasificación.
   */
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "team_id", nullable = false)
  private Team team;

  /**
   * Nombre del equipo o rival evaluado en la línea de clasificación.
   */
  @Column(name = "opponent_name", nullable = false, length = 150)
  private String opponentName;

  /**
   * Partidos jugados.
   */
  @Column(nullable = false)
  private Integer played = 0;

  /**
   * Partidos ganados.
   */
  @Column(nullable = false)
  private Integer won = 0;

  /**
   * Partidos empatados.
   */
  @Column(nullable = false)
  private Integer drawn = 0;

  /**
   * Partidos perdidos.
   */
  @Column(nullable = false)
  private Integer lost = 0;

  /**
   * Goles a favor.
   */
  @Column(name = "goals_for", nullable = false)
  private Integer goalsFor = 0;

  /**
   * Goles en contra.
   */
  @Column(name = "goals_against", nullable = false)
  private Integer goalsAgainst = 0;

  /**
   * Puntos totales en la clasificación.
   */
  @Column(nullable = false)
  private Integer points = 0;

  /**
   * Posición actual en la tabla.
   */
  @Column(nullable = false)
  private Integer position;

  /**
   * Marca temporal de la última actualización de la clasificación.
   */
  @Column(name = "updated_at")
  private LocalDateTime updatedAt = LocalDateTime.now();
}
