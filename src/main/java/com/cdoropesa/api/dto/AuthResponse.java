package com.cdoropesa.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO para la respuesta con el token JWT tras un login exitoso.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {
  private String token;
}
