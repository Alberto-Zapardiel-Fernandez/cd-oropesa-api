package com.cdoropesa.api.service;

import com.cdoropesa.api.model.Venue;
import com.cdoropesa.api.repository.VenueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de lógica de negocio para la gestión de Instalaciones (Venue).
 * Maneja las operaciones de persistencia de campos y recintos deportivos.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Service
public class VenueService {

  private final VenueRepository venueRepository;

  /**
   * Inyección de dependencias por constructor.
   *
   * @param venueRepository Repositorio JPA para la entidad Venue.
   */
  public VenueService(VenueRepository venueRepository) {
    this.venueRepository = venueRepository;
  }

  /**
   * Obtiene el listado completo de todas las instalaciones registradas.
   *
   * @return Lista de objetos Venue.
   */
  @Transactional(readOnly = true)
  public List<Venue> findAllVenues() {
    return this.venueRepository.findAll();
  }

  /**
   * Busca una instalación específica por su identificador único UUID.
   *
   * @param id Identificador único de la instalación.
   * @return Un Optional con la instalación si se encuentra.
   */
  @Transactional(readOnly = true)
  public Optional<Venue> findVenueById(UUID id) {
    return this.venueRepository.findById(id);
  }

  /**
   * Guarda o actualiza una instalación en el sistema.
   *
   * @param venue Objeto Venue a persistir.
   * @return La entidad Venue guardada.
   */
  @Transactional
  public Venue saveVenue(Venue venue) {
    return this.venueRepository.save(venue);
  }

  /**
   * Elimina una instalación por su identificador.
   *
   * @param id Identificador único de la instalación a eliminar.
   */
  @Transactional
  public void deleteVenue(UUID id) {
    this.venueRepository.deleteById(id);
  }
}
