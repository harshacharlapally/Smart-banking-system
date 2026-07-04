package com.banking.bankingsystem.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI bankingApiInfo(){
        return new OpenAPI()
                .info(new Info()
                        .title("Smart Banking System API")
                        .version("1.0")
                        .description("Complete REST API for Banking " +
                                "System with JWT Authentication, " +
                                "Fund Transfer and Transaction " +
                                "History")
                        .contact(new Contact()
                                .name("Harsha")
                                .email("harshacharlapally@gmail.com")))

                //JWT AUthorization button in Swagger UI
                .addSecurityItem(
                        new SecurityRequirement()
                                .addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}
