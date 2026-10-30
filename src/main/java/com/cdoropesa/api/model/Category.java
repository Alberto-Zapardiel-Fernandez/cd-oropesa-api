package com.cdoropesa.api.model;

import com.cdoropesa.api.dto.CategoryRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

/**
 * Entidad JPA que representa la tabla 'categories' en la base de datos PostgreSQL.
 * Almacena las categorías base del club (ej: Cadete, Alevín) junto con sus restricciones de género y edad.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

  /**
   * Identificador único de la categoría (UUID v4).
   */
  @Id
  @GeneratedValue
  @UuidGenerator
  private UUID id;

  /**
   * Nombre descriptivo de la categoría (ej: "Cadete", "Alevín").
   */
  @Column(nullable = false, length = 50)
  private String name;

  /**
   * Tipo de género asignado a la categoría (MALE, FEMALE, MIXED).
   */
  @Enumerated(EnumType.STRING)
  @Column(name = "gender", columnDefinition = "gender_type", nullable = false)
  @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.NAMED_ENUM)
  private CategoryRequest.GenderEnum gender;

  /**
   * Edad mínima permitida para pertenecer a la categoría.
   */
  @Column(name = "min_age")
  private Integer minAge;

  /**
   * Edad máxima permitida para pertenecer a la categoría.
   */
  @Column(name = "max_age")
  private Integer maxAge;

  /**
   * Marca temporal de la creación del registro en el sistema (inmutable tras su creación).
   */
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt = LocalDateTime.now();
}
