package com.cdoropesa.api.controller;

import com.cdoropesa.api.dto.PlayerFileAccessResponse;
import com.cdoropesa.api.dto.PlayerResponse;
import com.cdoropesa.api.mapper.PlayerMapper;
import com.cdoropesa.api.model.Player;
import com.cdoropesa.api.service.PlayerFileStorageService;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/players")
public class PlayerFileStorageController {

  private final PlayerFileStorageService storageService;
  private final PlayerMapper playerMapper;

  public PlayerFileStorageController(
      PlayerFileStorageService storageService,
      PlayerMapper playerMapper
  ) {
    this.storageService = storageService;
    this.playerMapper = playerMapper;
  }

  @PostMapping(value = "/{id}/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<PlayerResponse> uploadPhoto(
      @PathVariable UUID id,
      @RequestPart("file") MultipartFile file
  ) {
    Player saved = this.storageService.store(id, PlayerFileStorageService.AssetType.PHOTO, file);
    return ResponseEntity.status(HttpStatus.OK).body(this.playerMapper.toResponse(saved));
  }

  @PostMapping(value = "/{id}/dni-document", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<PlayerResponse> uploadDniDocument(
      @PathVariable UUID id,
      @RequestPart("file") MultipartFile file
  ) {
    Player saved = this.storageService.store(id, PlayerFileStorageService.AssetType.DNI_DOCUMENT, file);
    return ResponseEntity.status(HttpStatus.OK).body(this.playerMapper.toResponse(saved));
  }

  @GetMapping("/{id}/photo-access")
  public ResponseEntity<PlayerFileAccessResponse> getPhotoAccess(@PathVariable UUID id) {
    String url = this.storageService.createSignedUrl(id, PlayerFileStorageService.AssetType.PHOTO);
    return ResponseEntity.ok(new PlayerFileAccessResponse(url, 300));
  }

  @GetMapping("/{id}/dni-document-access")
  public ResponseEntity<PlayerFileAccessResponse> getDniDocumentAccess(@PathVariable UUID id) {
    String url = this.storageService.createSignedUrl(id, PlayerFileStorageService.AssetType.DNI_DOCUMENT);
    return ResponseEntity.ok(new PlayerFileAccessResponse(url, 300));
  }
}
