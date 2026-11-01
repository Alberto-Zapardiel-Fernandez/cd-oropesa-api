package com.cdoropesa.api.service;

import com.cdoropesa.api.model.Coach;
import com.cdoropesa.api.model.PlayerDocument;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CoachFileStorageService {

  private final CoachService coachService;
  private final PlayerFileStorageService privateStorage;
  private final TeamStaffService teamStaffService;

  public CoachFileStorageService(
      CoachService coachService,
      PlayerFileStorageService privateStorage,
      TeamStaffService teamStaffService
  ) {
    this.coachService = coachService;
    this.privateStorage = privateStorage;
    this.teamStaffService = teamStaffService;
  }

  public Coach uploadPhoto(UUID coachId, MultipartFile file) {
    Coach coach = this.findCoach(coachId);
    coach.setPhotoUrl(this.privateStorage.storePrivatePhoto(coachId, file));
    return this.coachService.saveCoach(coach);
  }

  public String createSignedPhotoUrl(UUID coachId) {
    Coach coach = this.findCoach(coachId);
    return this.privateStorage.createSignedPrivatePhotoUrl(coachId, coach.getPhotoUrl());
  }

  public Coach deletePhoto(UUID coachId) {
    Coach coach = this.findCoach(coachId);
    this.privateStorage.deletePrivatePhoto(coachId, coach.getPhotoUrl());
    coach.setPhotoUrl(null);
    return this.coachService.saveCoach(coach);
  }

  public Coach uploadDocument(UUID coachId, String kind, MultipartFile file) {
    Coach coach = this.findCoach(coachId);
    PlayerFileStorageService.DocumentKind documentKind = this.privateStorage.parseDocumentKind(kind);
    List<PlayerDocument> documents = coach.getDocumentation() == null
        ? new ArrayList<>()
        : new ArrayList<>(coach.getDocumentation());
    PlayerDocument existing = documentKind == PlayerFileStorageService.DocumentKind.OTHER
        ? null
        : documents.stream()
            .filter(document -> documentKind.name().equals(document.getKind()))
            .findFirst()
            .orElse(null);
    PlayerDocument uploaded = this.privateStorage.uploadPrivateDocument(coachId, documentKind.name(), file, existing);
    if (existing != null) documents.remove(existing);
    documents.add(uploaded);
    coach.setDocumentation(documents);
    return this.coachService.saveCoach(coach);
  }

  public String createSignedDocumentUrl(UUID coachId, UUID documentId) {
    Coach coach = this.findCoach(coachId);
    PlayerDocument document = this.findDocument(coach, documentId);
    return this.privateStorage.createSignedPrivateDocumentUrl(coachId, document);
  }

  public Coach deleteDocument(UUID coachId, UUID documentId) {
    Coach coach = this.findCoach(coachId);
    PlayerDocument document = this.findDocument(coach, documentId);
    this.privateStorage.deletePrivateDocument(coachId, document);
    List<PlayerDocument> documents = new ArrayList<>(coach.getDocumentation());
    documents.remove(document);
    coach.setDocumentation(documents);
    return this.coachService.saveCoach(coach);
  }

  public void deleteCoach(UUID coachId) {
    Coach coach = this.findCoach(coachId);
    if (this.teamStaffService.hasAssignments(coachId)) {
      throw new ResponseStatusException(
          HttpStatus.CONFLICT,
          "Quita primero los cargos asignados a esta persona en sus equipos."
      );
    }
    if (coach.getPhotoUrl() != null) {
      this.privateStorage.deletePrivatePhoto(coachId, coach.getPhotoUrl());
    }
    if (coach.getDocumentation() != null) {
      for (PlayerDocument document : coach.getDocumentation()) {
        this.privateStorage.deletePrivateDocument(coachId, document);
      }
    }
    this.coachService.deleteCoach(coachId);
  }

  private Coach findCoach(UUID coachId) {
    return this.coachService.findCoachById(coachId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Miembro del cuerpo técnico no encontrado."));
  }

  private PlayerDocument findDocument(Coach coach, UUID documentId) {
    if (coach.getDocumentation() == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Documento no encontrado.");
    }
    return coach.getDocumentation().stream()
        .filter(document -> documentId.toString().equals(document.getId()))
        .findFirst()
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Documento no encontrado."));
  }
}
