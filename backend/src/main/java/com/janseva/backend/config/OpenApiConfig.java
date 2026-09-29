package com.janseva.backend.config;

import io.swagger.v3.oas.models.OpenAPI;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()

                .info(

                        new Info()

                                .title(
                                        "JanSeva AI Governance API"
                                )

                                .version(
                                        "1.0"
                                )

                                .description(
                                        "Real AI Powered Governance Complaint Management System"
                                )

                                .contact(

                                        new Contact()

                                                .name(
                                                        "Samir Sulakhe"
                                                )

                                                .email(
                                                        "samir@janseva.ai"
                                                )
                                )

                                .license(

                                        new License()

                                                .name(
                                                        "Apache 2.0"
                                                )
                                )
                );
    }
}