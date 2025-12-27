package com.onetuks.ihub.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenService {

  private static final String ROLES_CLAIM = "roles";

  private final Key signingKey;
  private final long expirationMillis;
  private final InMemoryTokenStore tokenStore;

  public JwtTokenService(
      @Value("${security.jwt.secret}")
      String secret,
      @Value("${security.jwt.expiration-ms}") long expirationMillis,
      InMemoryTokenStore tokenStore) {
    this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.expirationMillis = expirationMillis;
    this.tokenStore = tokenStore;
  }

  public JwtToken issueToken(AuthUserDetails userDetails) {
    Instant now = Instant.now();
    Instant expiresAt = now.plusMillis(expirationMillis);

    String token = Jwts.builder()
        .subject(userDetails.getUsername())
        .claim(ROLES_CLAIM, userDetails.getRoles())
        .issuedAt(Date.from(now))
        .expiration(Date.from(expiresAt))
        .signWith(signingKey)
        .compact();

    tokenStore.store(token, expiresAt);
    return new JwtToken(token, expiresAt);
  }

  public boolean isTokenActive(String token) {
    return tokenStore.isActive(token) && !isExpired(token);
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    String username = extractUsername(token);
    return username.equals(userDetails.getUsername()) && isTokenActive(token);
  }

  public String extractUsername(String token) {
    return parseClaims(token).getSubject();
  }

  public Instant extractExpiration(String token) {
    return parseClaims(token).getExpiration().toInstant();
  }

  public List<String> extractRoles(String token) {
    List<?> roles = parseClaims(token).get(ROLES_CLAIM, List.class);
    return roles == null ? List.of() : roles.stream().map(String::valueOf).toList();
  }

  public void revoke(String token) {
    tokenStore.revoke(token);
  }

  private boolean isExpired(String token) {
    Date expiration = parseClaims(token).getExpiration();
    return expiration.before(new Date());
  }

  private Claims parseClaims(String token) {
    return Jwts.parser()
        .verifyWith(signingKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  public record JwtToken(String value, Instant expiresAt) {
  }
}
