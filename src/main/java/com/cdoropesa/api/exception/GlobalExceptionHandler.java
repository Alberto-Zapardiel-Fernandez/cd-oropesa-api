package com.cdoropesa.api.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * Mananejador global de excepciones para interceptar fallos en los controladores
 * y transformarlos en respuestas JSON estandarizadas (ApiErrorResponse).
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Intercepta la excepción ResourceNotFoundException y devuelve HTTP 404 NOT FOUND.
   *
   * @param ex Excepción capturada.
   * @param request Información de la petición HTTP.
   * @return Respuesta estructurada de error ApiErrorResponse.
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ApiErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex,
                                                                          HttpServletRequest request) {
    ApiErrorResponse error = new ApiErrorResponse(
        HttpStatus.NOT_FOUND.value(),
        HttpStatus.NOT_FOUND.getReasonPhrase(),
        ex.getMessage(),
        request.getRequestURI(),
        null
    );
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  /**
   * Intercepta errores de validación de Bean Validation (@Valid) y devuelve HTTP 400 BAD REQUEST.
   *
   * @param ex Excepción capturada por fallos en las anotaciones de validación.
   * @param request Información de la petición HTTP.
   * @return Respuesta estructurada con los detalles de cada campo inválido.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex,
                                                                     HttpServletRequest request) {
    List<String> details = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(FieldError::getDefaultMessage)
        .toList();

    ApiErrorResponse error = new ApiErrorResponse(
        HttpStatus.BAD_REQUEST.value(),
        HttpStatus.BAD_REQUEST.getReasonPhrase(),
        "Error de validación en los campos recibidos",
        request.getRequestURI(),
        details
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  /**
   * Intercepta cualquier otra excepción no controlada y devuelve HTTP 500 INTERNAL SERVER ERROR.
   *
   * @param ex Excepción general capturada.
   * @param request Información de la petición HTTP.
   * @return Respuesta estructurada de error del servidor.
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiErrorResponse> handleGlobalException(Exception ex, HttpServletRequest request) {
    ApiErrorResponse error = new ApiErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
        "Ha ocurrido un error interno no esperado",
        request.getRequestURI(),
        List.of(ex.getMessage() != null ? ex.getMessage() : "Excepción no detallada")
    );
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }
}
