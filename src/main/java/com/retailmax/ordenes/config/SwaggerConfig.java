package com.retailmax.ordenes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customerOpenApi(){
        return new OpenAPI()
                .info(new Info()
                            .title("API ORDENES")
                            .version("1.0")
                            .description("Documentación de la API para el microservicio de Ordenes"));
    }


}

