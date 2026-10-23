package com.cdoropesa.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
 * Entidad JPA que representa la tabla 'teams' en la base de datos PostgreSQL.
 * Almacena los equipos del club inscritos en una temporada y categoría concreta.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Entity
@Table(name = "teams", uniqueConstraints = {
    @UniqueConstraint(name = "unique_team_season", columnNames = {"category_id", "season_id", "name"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Team {

  /**
   * Identificador único del equipo (UUID v4).
   */
  @Id
  @GeneratedValue
  @UuidGenerator
  private UUID id;

  /**
   * Categoría base a la que pertenece el equipo.
   */
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  /**
   * Temporada oficial en la que compite el equipo.
   */
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "season_id", nullable = false)
  private Season season;

  /**
   * Nombre descriptivo del equipo (ej: "Oropesa Cadete A").
   */
  @Column(nullable = false, length = 100)
  private String name;

  /**
   * Nombre del grupo de competición (ej: "Grupo B - Deporte Escolar Talavera").
   */
  @Column(name = "group_name", length = 100)
  private String groupName;

  /**
   * Marca temporal de la creación del registro en el sistema.
   */
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt = LocalDateTime.now();
}
