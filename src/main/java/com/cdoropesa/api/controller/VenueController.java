package com.cdoropesa.api.controller;

import com.cdoropesa.api.dto.VenueRequest;
import com.cdoropesa.api.dto.VenueResponse;
import com.cdoropesa.api.generated.VenuesApi;
import com.cdoropesa.api.mapper.VenueMapper;
import com.cdoropesa.api.model.Venue;
import com.cdoropesa.api.service.VenueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador REST para la gestión de las instalaciones deportivas y campos del club.
 * Implementa la interfaz contract-first generada automáticamente por OpenAPI (VenuesApi).
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1")
public class VenueController implements VenuesApi {

  private final VenueService venueService;
  private final VenueMapper venueMapper;

  /**
   * Inyección de dependencias mediante constructor.
   *
   * @param venueService Servicio de lógica de negocio para instalaciones.
   * @param venueMapper Mapeador MapStruct entre entidades y DTOs.
   */
  public VenueController(VenueService venueService, VenueMapper venueMapper) {
    this.venueService = venueService;
    this.venueMapper = venueMapper;
  }

  /**
   * Obtiene el listado completo de todas las instalaciones deportivas registradas.
   *
   * @return ResponseEntity con la lista de instalaciones en formato DTO y código HTTP 200.
   */
  @Override
  public ResponseEntity<List<VenueResponse>> getAllVenues() {
    List<Venue> venues = this.venueService.findAllVenues();
    return ResponseEntity.ok(this.venueMapper.toResponseList(venues));
  }

  /**
   * Registra y crea una nueva instalación deportiva en el sistema.
   *
   * @param venueRequest DTO con los datos de la instalación a registrar.
   * @return ResponseEntity con la instalación creada y código HTTP 201.
   */
  @Override
  public ResponseEntity<VenueResponse> createVenue(VenueRequest venueRequest) {
    Venue venue = this.venueMapper.toEntity(venueRequest);
    Venue saved = this.venueService.saveVenue(venue);
    return ResponseEntity.status(HttpStatus.CREATED).body(this.venueMapper.toResponse(saved));
  }
}
