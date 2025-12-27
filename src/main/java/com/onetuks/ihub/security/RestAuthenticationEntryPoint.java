package com.onetuks.ihub.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetuks.ihub.exception.ApiError;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper;

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException) throws IOException {

    String message = "Authentication is required to access this resource.";
    Object jwtException = request.getAttribute("jwtException");
    if (jwtException instanceof JwtException exception) {
      message = exception.getMessage();
    }

    ApiError apiError = ApiError.of(
        HttpServletResponse.SC_UNAUTHORIZED,
        "UNAUTHORIZED",
        message,
        Collections.emptyList(),
        request.getRequestURI());

    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getWriter().write(objectMapper.writeValueAsString(apiError));
  }
}
