package com.onetuks.ihub.security;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class InMemoryTokenStore {

  private final Map<String, Instant> activeTokens = new ConcurrentHashMap<>();

  public void store(String token, Instant expiresAt) {
    activeTokens.put(token, expiresAt);
  }

  public boolean isActive(String token) {
    Instant expiresAt = activeTokens.get(token);
    if (expiresAt == null) {
      return false;
    }
    if (Instant.now().isAfter(expiresAt)) {
      activeTokens.remove(token);
      return false;
    }
    return true;
  }

  public void revoke(String token) {
    activeTokens.remove(token);
  }
}
