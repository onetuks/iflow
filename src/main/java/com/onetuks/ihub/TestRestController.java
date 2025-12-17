package com.onetuks.ihub;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRestController {

  @GetMapping(path = "/health")
  public ResponseEntity<String> health() {
    return ResponseEntity.ok("very healthy");
  }
}
