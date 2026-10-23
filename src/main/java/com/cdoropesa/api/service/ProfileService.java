package com.cdoropesa.api.service;

import com.cdoropesa.api.model.Profile;
import com.cdoropesa.api.repository.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de lógica de negocio para la gestión de Perfiles de Usuario (Profile).
 * Maneja las operaciones de persistencia y reglas para los usuarios del sistema.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Service
public class ProfileService {

  private final ProfileRepository profileRepository;

  /**
   * Inyección de dependencias por constructor.
   *
   * @param profileRepository Repositorio JPA para la entidad Profile.
   */
  public ProfileService(ProfileRepository profileRepository) {
    this.profileRepository = profileRepository;
  }

  /**
   * Obtiene el listado completo de todos los perfiles de usuario.
   *
   * @return Lista de objetos Profile.
   */
  @Transactional(readOnly = true)
  public List<Profile> findAllProfiles() {
    return this.profileRepository.findAll();
  }

  /**
   * Busca un perfil específico por su identificador único UUID.
   *
   * @param id Identificador único del perfil.
   * @return Un Optional con el perfil si se encuentra.
   */
  @Transactional(readOnly = true)
  public Optional<Profile> findProfileById(UUID id) {
    return this.profileRepository.findById(id);
  }

  /**
   * Guarda o actualiza un perfil de usuario en el sistema.
   *
   * @param profile Objeto Profile a persistir.
   * @return La entidad Profile guardada.
   */
  @Transactional
  public Profile saveProfile(Profile profile) {
    return this.profileRepository.save(profile);
  }

  /**
   * Elimina un perfil de usuario por su identificador.
   *
   * @param id Identificador único del perfil a eliminar.
   */
  @Transactional
  public void deleteProfile(UUID id) {
    this.profileRepository.deleteById(id);
  }
}
