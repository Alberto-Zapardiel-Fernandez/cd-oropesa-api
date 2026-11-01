package com.cdoropesa.api.service;

import com.cdoropesa.api.model.Player;
import com.cdoropesa.api.model.PlayerDocument;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.UUID;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

@Service
public class PlayerFileStorageService {

  private static final long MAX_FILE_SIZE = 5L * 1024 * 1024;
  private static final int SIGNED_URL_EXPIRATION_SECONDS = 300;
  private static final long MAX_IMAGE_PIXELS = 40_000_000L;
  private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(10);
  private static final Duration RESPONSE_TIMEOUT = Duration.ofSeconds(30);

  private final PlayerService playerService;
  private final RestClient restClient;
  private final String supabaseUrl;
  private final String apiKey;
  private final String bucket;

  public PlayerFileStorageService(
      PlayerService playerService,
      @Value("${application.supabase.url:}") String supabaseUrl,
      @Value("${application.supabase.api-key:}") String apiKey,
      @Value("${application.supabase.bucket:player-private-files}") String bucket
  ) {
    this.playerService = playerService;
    HttpClient httpClient = HttpClient.newBuilder()
        .connectTimeout(CONNECT_TIMEOUT)
        .build();
    JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
    requestFactory.setReadTimeout(RESPONSE_TIMEOUT);
    this.restClient = RestClient.builder()
        .requestFactory(requestFactory)
        .build();
    this.supabaseUrl = supabaseUrl.replaceAll("/+$", "");
    this.apiKey = apiKey;
    this.bucket = bucket;
  }

  public Player store(UUID playerId, AssetType assetType, MultipartFile file) {
    Player player = this.findPlayer(playerId);
    this.ensureConfigured();

    if (file == null || file.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Selecciona un archivo.");
    }
    if (file.getSize() > MAX_FILE_SIZE) {
      throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "El archivo no puede superar 5 MB.");
    }

    byte[] content;
    try {
      content = file.getBytes();
    } catch (IOException exception) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se pudo leer el archivo enviado.", exception);
    }

    String contentType = this.detectAndValidateContentType(content, true);
    String objectPath = playerId + "/" + assetType.path;

    try {
      this.restClient.put()
          .uri(this.objectUrl(objectPath))
          .headers(this::applyApiKey)
          .header("x-upsert", "true")
          .contentType(MediaType.parseMediaType(contentType))
          .body(new ByteArrayResource(content))
          .retrieve()
          .toBodilessEntity();
    } catch (RestClientResponseException exception) {
      throw new ResponseStatusException(
          HttpStatus.BAD_GATEWAY,
          "Supabase Storage rechazó el archivo (" + exception.getStatusCode().value() + ").",
          exception
      );
    } catch (ResourceAccessException exception) {
      throw new ResponseStatusException(
          HttpStatus.GATEWAY_TIMEOUT,
          "Supabase Storage no respondió a tiempo al recibir el archivo. Comprueba la conexión y vuelve a intentarlo.",
          exception
      );
    }

    player.setPhotoUrl(objectPath);
    return this.playerService.savePlayer(player);
  }

  public Player storeDocument(UUID playerId, String kind, MultipartFile file) {
    Player player = this.findPlayer(playerId);
    DocumentKind documentKind = this.parseDocumentKind(kind);
    List<PlayerDocument> documents = player.getDocumentation() == null
        ? new ArrayList<>()
        : new ArrayList<>(player.getDocumentation());
    PlayerDocument existing = documentKind == DocumentKind.OTHER
        ? null
        : documents.stream()
            .filter(document -> documentKind.name().equals(document.getKind()))
            .findFirst()
            .orElse(null);
    PlayerDocument savedDocument = this.uploadPrivateDocument(playerId, kind, file, existing);
    if (existing != null) {
      documents.remove(existing);
    }
    documents.add(savedDocument);
    player.setDocumentation(documents);
    return this.playerService.savePlayer(player);
  }

  public PlayerDocument uploadPrivateDocument(
      UUID ownerId,
      String kind,
      MultipartFile file,
      PlayerDocument existing
  ) {
    this.ensureConfigured();
    DocumentKind documentKind = this.parseDocumentKind(kind);
    if (file == null || file.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Selecciona un archivo.");
    }
    if (file.getSize() > MAX_FILE_SIZE) {
      throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "El archivo no puede superar 5 MB.");
    }

    byte[] content;
    try {
      content = file.getBytes();
    } catch (IOException exception) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se pudo leer el archivo enviado.", exception);
    }
    String contentType = this.detectAndValidateContentType(content, false);
    if (documentKind != DocumentKind.OTHER && !contentType.startsWith("image/")) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Las caras del DNI deben subirse como imágenes JPEG o PNG."
      );
    }

    String documentId = existing == null ? UUID.randomUUID().toString() : existing.getId();
    String objectPath = ownerId + "/documentation/" + documentId;
    PlayerDocument document = new PlayerDocument(
        documentId,
        this.safeFileName(file.getOriginalFilename(), documentKind),
        documentKind.name(),
        contentType,
        objectPath
    );
    this.uploadObject(objectPath, content, contentType);
    return document;
  }

  public String storePrivatePhoto(UUID ownerId, MultipartFile file) {
    this.ensureConfigured();
    if (file == null || file.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Selecciona una fotografía.");
    }
    if (file.getSize() > MAX_FILE_SIZE) {
      throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "La fotografía no puede superar 5 MB.");
    }
    byte[] content;
    try {
      content = file.getBytes();
    } catch (IOException exception) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se pudo leer la fotografía enviada.", exception);
    }
    String contentType = this.detectAndValidateContentType(content, true);
    String objectPath = ownerId + "/photo";
    this.uploadObject(objectPath, content, contentType);
    return objectPath;
  }

  public String createSignedPrivatePhotoUrl(UUID ownerId, String storedPath) {
    this.ensureConfigured();
    String expectedPath = ownerId + "/photo";
    if (!expectedPath.equals(storedPath)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La fotografía no está guardada para esta persona.");
    }
    return this.createSignedUrlForPath(expectedPath);
  }

  public void deletePrivatePhoto(UUID ownerId, String storedPath) {
    String expectedPath = ownerId + "/photo";
    if (!expectedPath.equals(storedPath)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La fotografía no está guardada para esta persona.");
    }
    this.ensureConfigured();
    this.deleteObjects(List.of(expectedPath));
  }

  public String createSignedPrivateDocumentUrl(UUID ownerId, PlayerDocument document) {
    this.ensurePrivateDocumentPath(ownerId, document);
    this.ensureConfigured();
    return this.createSignedUrlForPath(document.getStoragePath());
  }

  public void deletePrivateDocument(UUID ownerId, PlayerDocument document) {
    this.ensurePrivateDocumentPath(ownerId, document);
    this.ensureConfigured();
    this.deleteObjects(List.of(document.getStoragePath()));
  }

  public String createSignedDocumentUrl(UUID playerId, String documentId) {
    Player player = this.findPlayer(playerId);
    PlayerDocument document = this.findDocument(player, documentId);
    this.ensureConfigured();
    return this.createSignedUrlForPath(document.getStoragePath());
  }

  public Player deleteDocument(UUID playerId, String documentId) {
    Player player = this.findPlayer(playerId);
    this.ensureConfigured();
    PlayerDocument document = this.findDocument(player, documentId);

    this.deleteObjects(List.of(document.getStoragePath()));
    List<PlayerDocument> documents = new ArrayList<>(player.getDocumentation());
    documents.remove(document);
    player.setDocumentation(documents);
    return this.playerService.savePlayer(player);
  }

  public String createSignedUrl(UUID playerId, AssetType assetType) {
    Player player = this.findPlayer(playerId);
    this.ensureConfigured();

    String storedPath = player.getPhotoUrl();
    String expectedPath = playerId + "/" + assetType.path;
    if (!expectedPath.equals(storedPath)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El archivo no está guardado para este jugador.");
    }

    return this.createSignedUrlForPath(expectedPath);
  }

  private String createSignedUrlForPath(String objectPath) {
    try {
      Map<?, ?> response = this.restClient.post()
          .uri(this.signedUrlEndpoint(objectPath))
          .headers(this::applyApiKey)
          .contentType(MediaType.APPLICATION_JSON)
          .body(Map.of("expiresIn", SIGNED_URL_EXPIRATION_SECONDS))
          .retrieve()
          .body(Map.class);

      String signedPath = this.readSignedPath(response, "signedURL");
      if (signedPath.isBlank()) signedPath = this.readSignedPath(response, "signedUrl");
      if (signedPath.isBlank()) {
        throw new ResponseStatusException(
            HttpStatus.BAD_GATEWAY,
            "Supabase no devolvió un enlace temporal para el archivo."
        );
      }

      if (signedPath.startsWith("http://") || signedPath.startsWith("https://")) return signedPath;
      if (signedPath.startsWith("/storage/v1/")) return this.supabaseUrl + signedPath;
      return this.supabaseUrl + "/storage/v1" + (signedPath.startsWith("/") ? "" : "/") + signedPath;
    } catch (RestClientResponseException exception) {
      throw new ResponseStatusException(
          HttpStatus.BAD_GATEWAY,
          "No se pudo generar un enlace temporal de Supabase Storage.",
          exception
      );
    } catch (ResourceAccessException exception) {
      throw new ResponseStatusException(
          HttpStatus.GATEWAY_TIMEOUT,
          "Supabase Storage no respondió a tiempo al generar el enlace temporal.",
          exception
      );
    }
  }

  private void uploadObject(String objectPath, byte[] content, String contentType) {
    try {
      this.restClient.put()
          .uri(this.objectUrl(objectPath))
          .headers(this::applyApiKey)
          .header("x-upsert", "true")
          .contentType(MediaType.parseMediaType(contentType))
          .body(new ByteArrayResource(content))
          .retrieve()
          .toBodilessEntity();
    } catch (RestClientResponseException exception) {
      throw new ResponseStatusException(
          HttpStatus.BAD_GATEWAY,
          "Supabase Storage rechazó el archivo (" + exception.getStatusCode().value() + ").",
          exception
      );
    } catch (ResourceAccessException exception) {
      throw new ResponseStatusException(
          HttpStatus.GATEWAY_TIMEOUT,
          "Supabase Storage no respondió a tiempo al recibir el archivo.",
          exception
      );
    }
  }

  private void deleteObjects(List<String> paths) {
    try {
      this.restClient.method(HttpMethod.DELETE)
          .uri(this.bucketObjectsUrl())
          .headers(this::applyApiKey)
          .contentType(MediaType.APPLICATION_JSON)
          .body(Map.of("prefixes", paths))
          .retrieve()
          .toBodilessEntity();
    } catch (RestClientResponseException exception) {
      throw new ResponseStatusException(
          HttpStatus.BAD_GATEWAY,
          "Supabase Storage no pudo eliminar el archivo (" + exception.getStatusCode().value() + ").",
          exception
      );
    } catch (ResourceAccessException exception) {
      throw new ResponseStatusException(
          HttpStatus.GATEWAY_TIMEOUT,
          "Supabase Storage no respondió a tiempo al eliminar el archivo.",
          exception
      );
    }
  }

  public Player delete(UUID playerId, AssetType assetType) {
    Player player = this.findPlayer(playerId);
    this.ensureConfigured();

    String storedPath = player.getPhotoUrl();
    String expectedPath = playerId + "/" + assetType.path;
    if (!expectedPath.equals(storedPath)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El archivo no está guardado para este jugador.");
    }

    this.deleteObjects(List.of(expectedPath));
    player.setPhotoUrl(null);
    return this.playerService.savePlayer(player);
  }

  private String readSignedPath(Map<?, ?> response, String key) {
    if (response == null) return "";
    Object value = response.get(key);
    return value instanceof String path ? path : "";
  }

  private PlayerDocument findDocument(Player player, String documentId) {
    if (player.getDocumentation() == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Documento no encontrado.");
    }
    return player.getDocumentation().stream()
            .filter(document -> documentId.equals(document.getId()))
            .findFirst()
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Documento no encontrado."));
  }

  public DocumentKind parseDocumentKind(String value) {
    try {
      return DocumentKind.valueOf(value);
    } catch (IllegalArgumentException | NullPointerException exception) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Tipo de documento no válido. Usa DNI_FRONT, DNI_BACK o OTHER.",
          exception
      );
    }
  }

  private String safeFileName(String fileName, DocumentKind kind) {
    if (fileName == null || fileName.isBlank()) {
      return kind == DocumentKind.OTHER ? "documento" : "dni-" + kind.name().toLowerCase();
    }
    String normalized = fileName.replace('\\', '/');
    String baseName = normalized.substring(normalized.lastIndexOf('/') + 1);
    String safeName = baseName.replaceAll("[\\p{Cntrl}]", "").trim();
    if (safeName.isBlank()) return kind.name().toLowerCase();
    return safeName.length() > 200 ? safeName.substring(safeName.length() - 200) : safeName;
  }

  private void ensurePrivateDocumentPath(UUID ownerId, PlayerDocument document) {
    if (document == null
        || document.getStoragePath() == null
        || !document.getStoragePath().startsWith(ownerId + "/documentation/")) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Documento no encontrado.");
    }
  }

  private Player findPlayer(UUID playerId) {
    return this.playerService.findPlayerById(playerId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Jugador no encontrado."));
  }

  private void ensureConfigured() {
    if (this.supabaseUrl.isBlank() || this.apiKey.isBlank() || this.bucket.isBlank()) {
      throw new ResponseStatusException(
          HttpStatus.SERVICE_UNAVAILABLE,
          "La integración de almacenamiento de archivos no está configurada."
      );
    }
  }

  private void applyApiKey(HttpHeaders headers) {
    headers.set("apikey", this.apiKey);
    if (this.apiKey.startsWith("eyJ")) {
      headers.setBearerAuth(this.apiKey);
    }
  }

  private String detectAndValidateContentType(byte[] content, boolean photoOnly) {
    String contentType;
    if (this.hasPrefix(content, new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF})) {
      contentType = "image/jpeg";
    } else if (this.hasPrefix(content, new byte[] {
        (byte) 0x89, 'P', 'N', 'G', 0x0D, 0x0A, 0x1A, 0x0A
    })) {
      contentType = "image/png";
    } else if (this.hasPrefix(content, "%PDF-".getBytes(StandardCharsets.US_ASCII))) {
      contentType = "application/pdf";
    } else {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Formato no admitido. Usa JPEG o PNG; para el documento también se admite PDF."
      );
    }

    if (photoOnly && !contentType.startsWith("image/")) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La foto debe ser una imagen JPEG o PNG.");
    }

    if (contentType.startsWith("image/")) {
      this.validateImage(content);
    }
    return contentType;
  }

  private void validateImage(byte[] content) {
    try (ByteArrayInputStream input = new ByteArrayInputStream(content)) {
      BufferedImage image = ImageIO.read(input);
      if (image == null) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El archivo no contiene una imagen válida.");
      }
      long pixels = (long) image.getWidth() * image.getHeight();
      if (image.getWidth() <= 0 || image.getHeight() <= 0 || pixels > MAX_IMAGE_PIXELS) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La resolución de la imagen es demasiado grande.");
      }
    } catch (IOException exception) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se pudo validar la imagen.", exception);
    }
  }

  private boolean hasPrefix(byte[] content, byte[] prefix) {
    if (content.length < prefix.length) return false;
    for (int index = 0; index < prefix.length; index++) {
      if (content[index] != prefix[index]) return false;
    }
    return true;
  }

  private String objectUrl(String path) {
    return this.supabaseUrl + "/storage/v1/object/"
        + UriUtils.encodePathSegment(this.bucket, StandardCharsets.UTF_8) + "/"
        + UriUtils.encodePath(path, StandardCharsets.UTF_8);
  }

  private String signedUrlEndpoint(String path) {
    return this.supabaseUrl + "/storage/v1/object/sign/"
        + UriUtils.encodePathSegment(this.bucket, StandardCharsets.UTF_8) + "/"
        + UriUtils.encodePath(path, StandardCharsets.UTF_8);
  }

  private String bucketObjectsUrl() {
    return this.supabaseUrl + "/storage/v1/object/"
        + UriUtils.encodePathSegment(this.bucket, StandardCharsets.UTF_8);
  }

  public enum AssetType {
    PHOTO("photo");

    private final String path;

    AssetType(String path) {
      this.path = path;
    }

  }

  public enum DocumentKind {
    DNI_FRONT,
    DNI_BACK,
    OTHER
  }
}
