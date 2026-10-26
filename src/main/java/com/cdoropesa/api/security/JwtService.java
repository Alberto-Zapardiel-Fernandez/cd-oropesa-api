package com.cdoropesa.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

/**
 * Servicio encargado de la generación, validación y procesamiento de tokens JWT.
 *
 * @author CD Oropesa Dev Team
 * @version 1.0
 */
@Service
public class JwtService {

  @Value("${application.security.jwt.secret-key}")
  private String secretKey;

  @Value("${application.security.jwt.expiration:900000}") // 15 minutos por defecto
  private long jwtExpiration;

  /**
   * Extrae el nombre de usuario (subject) contenido dentro del token JWT.
   *
   * @param token Token JWT en formato String.
   * @return Nombre de usuario.
   */
  public String extractUsername(String token) {
    return this.extractClaim(token, Claims::getSubject);
  }

  /**
   * Extrae un claim específico del token utilizando una función resolutoria.
   *
   * @param token Token JWT.
   * @param claimsResolver Función para extraer el claim deseado.
   * @param <T> Tipo de dato devuelto por el claim.
   * @return Valor del claim.
   */
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = this.extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  /**
   * Genera un token JWT para un usuario autenticado.
   *
   * @param userDetails Detalles del usuario del sistema.
   * @return Token JWT generado como cadena de texto.
   */
  public String generateToken(UserDetails userDetails) {
    return this.buildToken(userDetails);
  }

  /**
   * Construye el token JWT añadiendo la fecha de emisión, expiración y firma criptográfica.
   *
   * @param userDetails Detalles del usuario.
   * @return Token JWT firmado.
   */
  private String buildToken(UserDetails userDetails) {
    return Jwts.builder()
        .subject(userDetails.getUsername())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + this.jwtExpiration))
        .signWith(this.getSignInKey(), Jwts.SIG.HS256)
        .compact();
  }

  /**
   * Valida si el token JWT es correcto y pertenece al usuario indicado.
   *
   * @param token Token JWT.
   * @param userDetails Detalles del usuario a verificar.
   * @return true si el token es válido, false en caso contrario.
   */
  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = this.extractUsername(token);
    return (username.equals(userDetails.getUsername())) && !this.isTokenExpired(token);
  }

  /**
   * Comprueba si el token JWT ha expirado.
   *
   * @param token Token JWT.
   * @return true si ya ha expirado.
   */
  private boolean isTokenExpired(String token) {
    return this.extractExpiration(token).before(new Date());
  }

  /**
   * Extrae la fecha de expiración del token.
   *
   * @param token Token JWT.
   * @return Fecha de expiración.
   */
  private Date extractExpiration(String token) {
    return this.extractClaim(token, Claims::getExpiration);
  }

  /**
   * Extrae todos los claims (cargas útiles) del token utilizando la clave secreta.
   *
   * @param token Token JWT.
   * @return Objeto Claims con la información interna.
   */
  private Claims extractAllClaims(String token) {
    return Jwts.parser()
        .verifyWith(this.getSignInKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  /**
   * Obtiene la clave criptográfica para firmar y verificar los tokens.
   *
   * @return SecretKey de tipo HMAC.
   */
  private SecretKey getSignInKey() {
    byte[] keyBytes = this.secretKey.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
