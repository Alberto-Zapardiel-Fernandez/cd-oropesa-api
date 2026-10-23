package com.cdoropesa.api.model;

import com.cdoropesa.api.model.enums.StaffRole;
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
 * Entidad JPA que representa la tabla 'team_staff' en la base de datos PostgreSQL.
 * Gestiona la asignación de los entrenadores y miembros del cuerpo técnico a los diferentes
 * equipos del club, especificando el rol que desempeñan (primer entrenador, delegado, etc.).
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Entity
@Table(name = "team_staff")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamStaff {

  /**
   * Identificador único de la asignación del staff (UUID v4).
   */
  @Id
  @GeneratedValue
  @UuidGenerator
  private UUID id;

  /**
   * Equipo al que se asigna el miembro del cuerpo técnico.
   */
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "team_id", nullable = false)
  private Team team;

  /**
   * Entrenador o técnico asignado.
   */
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "coach_id", nullable = false)
  private Coach coach;

  /**
   * Rol o función específica que realiza en este equipo (HEAD_COACH, ASSISTANT_COACH, DELEGATE).
   */
  @Enumerated(EnumType.STRING)
  @Column(name = "role_in_team", nullable = false)
  private StaffRole roleInTeam = StaffRole.HEAD_COACH;

  /**
   * Marca temporal inmutable de la creación del registro.
   */
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt = LocalDateTime.now();
}
