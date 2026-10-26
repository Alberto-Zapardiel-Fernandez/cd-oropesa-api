package com.cdoropesa.api.exception;

/**
 * Excepción personalizada para recursos no encontrados en la capa de negocio.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
public class ResourceNotFoundException extends RuntimeException {

  /**
   * Construye la excepción con un mensaje descriptivo.
   *
   * @param message Mensaje que detalla el recurso no encontrado.
   */
  public ResourceNotFoundException(String message) {
    super(message);
  }
}
