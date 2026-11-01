package com.cdoropesa.api.model;

import com.cdoropesa.api.model.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Entidad JPA que representa a los usuarios del sistema con permisos de acceso
 * y datos de perfil básicos.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 150)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(name = "first_name", length = 100)
  private String firstName;

  @Column(name = "last_name", length = 150)
  private String lastName;

  @Column(name = "address")
  private String address;

  @Column(name = "city")
  private String city;

  @Column(name = "province")
  private String province;

  @Column(name = "country")
  private String country;

  @Column(name = "phone", length = 30)
  private String phone;

  @Enumerated(EnumType.STRING)
  @Column(name = "role", nullable = false, length = 20)
  private UserRole role = UserRole.VISITOR;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
  }

  @Override
  public String getUsername() {
    return this.email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}