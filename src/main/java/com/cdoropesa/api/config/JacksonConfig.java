package com.cdoropesa.api.config;

import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Jackson para gestionar tipos nullable.
 */
@Configuration
public class JacksonConfig {

  @Bean
  public JsonNullableModule jsonNullableModule() {
    return new JsonNullableModule();
  }
}
