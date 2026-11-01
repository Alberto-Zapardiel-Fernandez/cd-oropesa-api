package com.cdoropesa.api.controller;

import com.cdoropesa.api.dto.CoachRequest;
import com.cdoropesa.api.dto.CoachResponse;
import com.cdoropesa.api.dto.PlayerFileAccessResponse;
import com.cdoropesa.api.generated.CoachesApi;
import com.cdoropesa.api.mapper.CoachMapper;
import com.cdoropesa.api.model.Coach;
import com.cdoropesa.api.model.Profile;
import com.cdoropesa.api.model.enums.CoachStatus;
import com.cdoropesa.api.service.CoachService;
import com.cdoropesa.api.service.CoachFileStorageService;
import com.cdoropesa.api.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.net.URI;
import java.util.UUID;

/**
 * Controlador REST para la gestión del maestro de Entrenadores del club.
 * Implementa la interfaz contract-first generada automáticamente por OpenAPI (CoachesApi).
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1")
public class CoachController implements CoachesApi {

  private final CoachService coachService;
  private final ProfileService profileService;
  private final CoachMapper coachMapper;
  private final CoachFileStorageService coachFileStorageService;

  /**
   * Inyección de dependencias mediante constructor.
   *
   * @param coachService Servicio de lógica de negocio para entrenadores.
   * @param coachMapper Mapeador MapStruct entre entidades y DTOs.
   */
  public CoachController(
      CoachService coachService,
      ProfileService profileService,
      CoachMapper coachMapper,
      CoachFileStorageService coachFileStorageService
  ) {
    this.coachService = coachService;
    this.profileService = profileService;
    this.coachMapper = coachMapper;
    this.coachFileStorageService = coachFileStorageService;
  }

  /**
   * Obtiene el listado completo de todos los entrenadores registrados.
   *
   * @return ResponseEntity con la lista de entrenadores en formato DTO y código HTTP 200.
   */
  @Override
  public ResponseEntity<List<CoachResponse>> getAllCoaches() {
    List<Coach> coaches = this.coachService.findAllCoaches();
    return ResponseEntity.ok(this.coachMapper.toResponseList(coaches));
  }

  /**
   * Registra y crea un nuevo entrenador en el sistema.
   *
   * @param coachRequest DTO con los datos del entrenador a registrar.
   * @return ResponseEntity con el entrenador creado y código HTTP 201.
   */
  @Override
  public ResponseEntity<CoachResponse> createCoach(CoachRequest coachRequest) {
    Coach coach = this.coachMapper.toEntity(coachRequest);
    Profile profile = findProfile(coachRequest.getUserId());
    if (coachRequest.getUserId() != null && profile == null) {
      return ResponseEntity.badRequest().build();
    }
    coach.setUser(profile);
    if (coach.getStatus() == null) {
      coach.setStatus(CoachStatus.ACTIVE);
    }
    Coach saved = this.coachService.saveCoach(coach);
    return ResponseEntity.status(HttpStatus.CREATED).body(this.coachMapper.toResponse(saved));
  }

  @Override
  public ResponseEntity<CoachResponse> getCoachById(UUID id) {
    return this.coachService.findCoachById(id)
        .map(coach -> ResponseEntity.ok(this.coachMapper.toResponse(coach)))
        .orElse(ResponseEntity.notFound().build());
  }

  @Override
  public ResponseEntity<CoachResponse> updateCoach(UUID id, CoachRequest coachRequest) {
    return this.coachService.findCoachById(id)
        .map(existingCoach -> {
          Profile profile = findProfile(coachRequest.getUserId());
          if (coachRequest.getUserId() != null && profile == null) {
            return ResponseEntity.badRequest().<CoachResponse>build();
          }

          Coach updatedCoach = this.coachMapper.toEntity(coachRequest);
          updatedCoach.setId(existingCoach.getId());
          updatedCoach.setCreatedAt(existingCoach.getCreatedAt());
          updatedCoach.setPhotoUrl(existingCoach.getPhotoUrl());
          updatedCoach.setDocumentation(existingCoach.getDocumentation());
          updatedCoach.setUser(profile);
          if (updatedCoach.getStatus() == null) {
            updatedCoach.setStatus(CoachStatus.ACTIVE);
          }
          Coach saved = this.coachService.saveCoach(updatedCoach);
          return ResponseEntity.ok(this.coachMapper.toResponse(saved));
        })
        .orElse(ResponseEntity.notFound().build());
  }

  @Override
  public ResponseEntity<Void> deleteCoach(UUID id) {
    if (this.coachService.findCoachById(id).isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    this.coachFileStorageService.deleteCoach(id);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<CoachResponse> uploadCoachPhoto(UUID id, MultipartFile file) {
    return ResponseEntity.ok(this.coachMapper.toResponse(this.coachFileStorageService.uploadPhoto(id, file)));
  }

  @Override
  public ResponseEntity<CoachResponse> deleteCoachPhoto(UUID id) {
    return ResponseEntity.ok(this.coachMapper.toResponse(this.coachFileStorageService.deletePhoto(id)));
  }

  @Override
  public ResponseEntity<PlayerFileAccessResponse> getCoachPhotoAccess(UUID id) {
    String url = this.coachFileStorageService.createSignedPhotoUrl(id);
    return ResponseEntity.ok(new PlayerFileAccessResponse(URI.create(url), 300));
  }

  @Override
  public ResponseEntity<CoachResponse> uploadCoachDocument(UUID id, MultipartFile file, String kind) {
    return ResponseEntity.ok(
        this.coachMapper.toResponse(this.coachFileStorageService.uploadDocument(id, kind, file))
    );
  }

  @Override
  public ResponseEntity<PlayerFileAccessResponse> getCoachDocumentAccess(UUID id, UUID documentId) {
    String url = this.coachFileStorageService.createSignedDocumentUrl(id, documentId);
    return ResponseEntity.ok(new PlayerFileAccessResponse(URI.create(url), 300));
  }

  @Override
  public ResponseEntity<CoachResponse> deleteCoachDocument(UUID id, UUID documentId) {
    return ResponseEntity.ok(
        this.coachMapper.toResponse(this.coachFileStorageService.deleteDocument(id, documentId))
    );
  }

  private Profile findProfile(UUID id) {
    return id == null ? null : this.profileService.findProfileById(id).orElse(null);
  }
}
