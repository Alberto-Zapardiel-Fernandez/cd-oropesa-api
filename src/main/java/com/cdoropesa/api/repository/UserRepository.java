package com.cdoropesa.api.repository;

import com.cdoropesa.api.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la gestión de acceso a datos de los usuarios.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

  /**
   * Busca un usuario por su correo electrónico.
   *
   * @param email Correo electrónico del usuario.
   * @return Optional con la entidad si existe.
   */
  Optional<UserEntity> findByEmail(String email);
}
