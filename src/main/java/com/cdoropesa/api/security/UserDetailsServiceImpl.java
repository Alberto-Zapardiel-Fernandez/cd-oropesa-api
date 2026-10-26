package com.cdoropesa.api.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementación del servicio de carga de usuarios para Spring Security.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // TODO: Conectar con el repositorio de usuarios de la base de datos en el siguiente sprint.
    throw new UsernameNotFoundException("Usuario no encontrado con el email: " + username);
  }
}