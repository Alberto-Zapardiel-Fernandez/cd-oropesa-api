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
 * Entidad JPA que representa la tabla 'players' en la base de datos PostgreSQL.
 * Almacena los datos personales, de contacto, dirección de residencia y documentación de los jugadores del club.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Entity
@Table(name = "players")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {

  /**
   * Identificador único del jugador (UUID v4).
   */
  @Id
  @GeneratedValue
  @UuidGenerator
  private UUID id;

  /**
   * Nombre de pila del jugador.
   */
  @Column(name = "first_name", nullable = false, length = 100)
  private String firstName;

  /**
   * Apellidos del jugador.
   */
  @Column(name = "last_name", nullable = false, length = 100)
  private String lastName;

  /**
   * Fecha de nacimiento oficial del jugador.
   */
  @Column(name = "birth_date", nullable = false)
  private LocalDate birthDate;

  /**
   * Documento Nacional de Identidad (DNI) o NIE.
   */
  @Column(length = 20)
  private String dni;

  /**
   * Número de licencia federativa.
   */
  @Column(name = "license_number", length = 50)
  private String licenseNumber;

  /**
   * Dirección física o domicilio de residencia del jugador.
   */
  @Column(columnDefinition = "TEXT")
  private String address;

  /**
   * URL o ruta de almacenamiento de la fotografía oficial.
   */
  @Column(name = "photo_url", columnDefinition = "TEXT")
  private String photoUrl;

  /**
   * URL o ruta del documento DNI escaneado.
   */
  @Column(name = "dni_file_url", columnDefinition = "TEXT")
  private String dniFileUrl;

  /**
   * Consentimiento firmado para la Ley Orgánica de Protección de Datos (RGPD).
   */
  @Column(name = "gdpr_consent")
  private Boolean gdprConsent = false;

  /**
   * Nombre completo del padre, madre o tutor legal (para menores de edad).
   */
  @Column(name = "guardian_name", length = 150)
  private String guardianName;

  /**
   * Teléfono de contacto del tutor legal.
   */
  @Column(name = "guardian_phone", length = 20)
  private String guardianPhone;

  /**
   * Correo electrónico de contacto del tutor legal.
   */
  @Column(name = "guardian_email", length = 255)
  private String guardianEmail;

  /**
   * Marca temporal inmutable de la creación del registro.
   */
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt = LocalDateTime.now();
}
