package com.cdoropesa.api.exception;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO estandarizado para representar las respuestas de error de la API REST.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Getter
public class ApiErrorResponse {

  private final LocalDateTime timestamp;
  private final int status;
  private final String error;
  private final String message;
  private final String path;
  private final List<String> details;

  /**
   * Constructor completo para inicializar la respuesta de error.
   *
   * @param status Código HTTP del error.
   * @param error Descripción corta del estado HTTP.
   * @param message Mensaje descriptivo del error.
   * @param path Ruta endpoint donde ocurrió la excepción.
   * @param details Lista opcional con detalles específicos del error (ej. validaciones).
   */
  public ApiErrorResponse(int status, String error, String message, String path, List<String> details) {
    this.timestamp = LocalDateTime.now();
    this.status = status;
    this.error = error;
    this.message = message;
    this.path = path;
    this.details = details;
  }

}