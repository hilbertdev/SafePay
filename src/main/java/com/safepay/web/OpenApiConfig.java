package com.safepay.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/**
 * Ring: web adapter
 * OpenAPI / Swagger UI metadata for demos.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI safePayOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SafePay API")
                        .description("Transaction submission and risk assessment endpoints.")
                        .version("1.0"));
    }
}
