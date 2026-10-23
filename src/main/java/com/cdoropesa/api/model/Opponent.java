package com.cdoropesa.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
 * Entidad JPA que representa la tabla 'opponents' en la base de datos PostgreSQL.
 * Almacena el directorio de clubes rivales y contrincantes, así como sus datos de contacto institucionales.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Entity
@Table(name = "opponents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Opponent {

  /**
   * Identificador único del club rival (UUID v4).
   */
  @Id
  @GeneratedValue
  @UuidGenerator
  private UUID id;

  /**
   * Nombre oficial del club rival.
   */
  @Column(nullable = false, length = 150)
  private String name;

  /**
   * Persona de contacto institucional en el club rival.
   */
  @Column(name = "contact_person", length = 100)
  private String contactPerson;

  /**
   * Teléfono de contacto del rival.
   */
  @Column(name = "contact_phone", length = 20)
  private String contactPhone;

  /**
   * Correo electrónico de contacto institucional.
   */
  @Column(name = "contact_email", length = 255)
  private String contactEmail;

  /**
   * Notas u observaciones adicionales sobre el club rival.
   */
  @Column(columnDefinition = "TEXT")
  private String notes;

  /**
   * Marca temporal inmutable de la creación del registro.
   */
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt = LocalDateTime.now();
}
