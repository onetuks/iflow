package com.onetuks.ihub.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI ihubOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("IHub API")
            .version("v1")
            .description("iHub API documentation"));
  }
}
