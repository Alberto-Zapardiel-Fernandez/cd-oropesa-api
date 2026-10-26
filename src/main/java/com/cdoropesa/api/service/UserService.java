package com.cdoropesa.api.service;

import com.cdoropesa.api.exception.ResourceNotFoundException;
import com.cdoropesa.api.model.UserEntity;
import com.cdoropesa.api.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Lógica de negocio para la gestión y consulta de usuarios del sistema.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Transactional(readOnly = true)
  public List<UserEntity> getAllUsers() {
    return this.userRepository.findAll();
  }

  @Transactional(readOnly = true)
  public UserEntity getUserById(Long id) {
    return this.userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
  }
}
