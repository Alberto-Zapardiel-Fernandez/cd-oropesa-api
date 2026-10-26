package com.cdoropesa.api.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO para la petición de inicio de sesión.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Getter
@Setter
public class LoginRequest {
  private String email;
  private String password;
}
