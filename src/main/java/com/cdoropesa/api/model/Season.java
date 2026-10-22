package com.cdoropesa.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

/**
 * Entidad JPA que representa la tabla 'seasons' en la base de datos PostgreSQL.
 * Almacena la información relativa a las temporadas oficiales del CD Oropesa.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Entity
@Table(name = "seasons")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Season {

  /**
   * Identificador único de la temporada (UUID v4).
   */
  @Id
  @GeneratedValue
  @UuidGenerator
  private UUID id;

  /**
   * Nombre descriptivo y único de la temporada (ej: "2026/2027").
   */
  @Column(nullable = false, unique = true, length = 20)
  private String name;

  /**
   * Fecha de inicio oficial de la temporada.
   */
  @Column(name = "start_date", nullable = false)
  private LocalDate startDate;

  /**
   * Fecha de finalización oficial de la temporada.
   */
  @Column(name = "end_date", nullable = false)
  private LocalDate endDate;

  /**
   * Indicador booleano que señala si es la temporada activa actualmente en el club.
   */
  @Column(name = "is_current")
  private Boolean isCurrent = false;

  /**
   * Marca temporal de la creación del registro en el sistema (inmutable tras su creación).
   */
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt = LocalDateTime.now();
}
