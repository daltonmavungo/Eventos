package com.eventos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API - Sistema de Gestão de Eventos")
                        .version("1.0.0")
                        .description("API REST para gerenciamento de eventos, usuários, convites e pagamentos.")
                        .contact(new Contact()
                                .name("Suporte")
                                .email("suporte@eventos.com")));
    }
}
