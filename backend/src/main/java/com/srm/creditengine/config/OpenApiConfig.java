package com.srm.creditengine.config;
import io.swagger.v3.oas.models.OpenAPI; import io.swagger.v3.oas.models.info.Info; import org.springframework.context.annotation.*;
@Configuration public class OpenApiConfig { @Bean OpenAPI openAPI(){return new OpenAPI().info(new Info().title("SRM Credit Engine API").version("v1").description("Pricing and settlement API for multi-currency receivables"));} }
