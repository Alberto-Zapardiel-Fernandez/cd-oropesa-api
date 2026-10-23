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
 * Entidad JPA que representa la tabla 'venues' en la base de datos PostgreSQL.
 * Almacena las instalaciones deportivas, recintos y campos de fútbol donde se disputan los encuentros,
 * incluyendo detalles de superficie y geolocalización.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Entity
@Table(name = "venues")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Venue {

  /**
   * Identificador único de la instalación (UUID v4).
   */
  @Id
  @GeneratedValue
  @UuidGenerator
  private UUID id;

  /**
   * Nombre del campo o complejo deportivo.
   */
  @Column(nullable = false, length = 150)
  private String name;

  /**
   * Dirección postal de la instalación.
   */
  @Column(length = 255)
  private String address;

  /**
   * Localidad o municipio donde se ubica.
   */
  @Column(nullable = false, length = 100)
  private String city;

  /**
   * Indicador de si es el campo propio/local del CD Oropesa.
   */
  @Column(name = "is_home_venue")
  private Boolean isHomeVenue = false;

  /**
   * Tipo de superficie o terreno de juego (ej: Césped Artificial, Natural, Tierra).
   */
  @Column(name = "surface_type", length = 100)
  private String surfaceType;

  /**
   * Enlace web a Google Maps para la ubicación exacta.
   */
  @Column(name = "google_maps_url", columnDefinition = "TEXT")
  private String googleMapsUrl;

  /**
   * URL de una fotografía o plano de las instalaciones.
   */
  @Column(name = "image_url", columnDefinition = "TEXT")
  private String imageUrl;

  /**
   * Notas operativas o recomendaciones sobre el campo.
   */
  @Column(columnDefinition = "TEXT")
  private String notes;

  /**
   * Marca temporal inmutable de la creación del registro.
   */
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt = LocalDateTime.now();
}
