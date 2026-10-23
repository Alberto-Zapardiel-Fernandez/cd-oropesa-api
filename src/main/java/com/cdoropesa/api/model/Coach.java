package com.cdoropesa.api.model;

import com.cdoropesa.api.model.enums.CoachStatus;
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
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

/**
 * Entidad JPA que representa la tabla 'coaches' en la base de datos PostgreSQL.
 * Almacena el maestro de entrenadores y técnicos del club, sus datos de contacto,
 * dirección postal, titulaciones y vinculación opcional con un perfil de usuario del sistema.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Entity
@Table(name = "coaches")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coach {

  /**
   * Identificador único del entrenador (UUID v4).
   */
  @Id
  @GeneratedValue
  @UuidGenerator
  private UUID id;

  /**
   * Perfil de usuario del sistema asociado para acceso a la plataforma (opcional).
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private Profile user;

  /**
   * Nombre de pila del entrenador.
   */
  @Column(name = "first_name", nullable = false, length = 100)
  private String firstName;

  /**
   * Apellidos del entrenador.
   */
  @Column(name = "last_name", nullable = false, length = 100)
  private String lastName;

  /**
   * DNI o NIE del entrenador.
   */
  @Column(nullable = false, length = 20)
  private String dni;

  /**
   * Número de teléfono de contacto.
   */
  @Column(nullable = false, length = 20)
  private String phone;

  /**
   * Dirección postal o domicilio de residencia.
   */
  @Column(columnDefinition = "TEXT")
  private String address;

  /**
   * Correo electrónico de contacto principal.
   */
  @Column(nullable = false, length = 255)
  private String email;

  /**
   * Número de licencia federativa como técnico.
   */
  @Column(name = "license_number", length = 50)
  private String licenseNumber;

  /**
   * Titulación oficial o nivel académico deportivo (ej. UEFA Pro, Nivel 2).
   */
  @Column(length = 100)
  private String qualification;

  /**
   * URL de la fotografía del entrenador.
   */
  @Column(name = "photo_url", columnDefinition = "TEXT")
  private String photoUrl;

  /**
   * URL del documento DNI escaneado.
   */
  @Column(name = "dni_file_url", columnDefinition = "TEXT")
  private String dniFileUrl;

  /**
   * Estado operativo del entrenador en el club (ACTIVE, INACTIVE).
   */
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private CoachStatus status = CoachStatus.ACTIVE;

  /**
   * Marca temporal inmutable de la creación del registro.
   */
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt = LocalDateTime.now();
}
