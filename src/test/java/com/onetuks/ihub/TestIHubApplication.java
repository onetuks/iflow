package com.onetuks.ihub;

import org.springframework.boot.SpringApplication;

public class TestIHubApplication {

  public static void main(String[] args) {
    SpringApplication
        .from(IHubApplication::main)
        .with(TestcontainersConfiguration.class)
        .run(args);
  }

}
