package com.cdoropesa.api.model;

import com.cdoropesa.api.model.enums.UserRole;
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
 * Entidad JPA que representa la tabla 'profiles' en la base de datos PostgreSQL.
 * Almacena los perfiles de usuario y credenciales de acceso al sistema del CD Oropesa.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Entity
@Table(name = "profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profile {

  /**
   * Identificador único del perfil de usuario (UUID v4).
   */
  @Id
  @GeneratedValue
  @UuidGenerator
  private UUID id;

  /**
   * Correo electrónico único del usuario (usado para la autenticación).
   */
  @Column(nullable = false, unique = true, length = 255)
  private String email;

  /**
   * Nombre del usuario.
   */
  @Column(name = "first_name", nullable = false, length = 100)
  private String firstName;

  /**
   * Apellidos del usuario.
   */
  @Column(name = "last_name", nullable = false, length = 100)
  private String lastName;

  /**
   * Rol asignado dentro de la plataforma (ADMIN, COORDINATOR, COACH, VISITOR).
   */
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private UserRole role = UserRole.VISITOR;

  /**
   * Marca temporal de la creación del registro en el sistema.
   */
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt = LocalDateTime.now();
}
