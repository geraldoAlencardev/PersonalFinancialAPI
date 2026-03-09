package com.Alencar.demo.infrastructure;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // 1. Configura o botão de "Authorize" para receber o Token JWT
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                // 2. Informações gerais da sua API
                .info(new Info()
                        .title("PersonalFinancerControlAPI")
                        .description("API REST para controle financeiro pessoal. Contém CRUD de usuários, contas, categorias e transações com segurança JWT.")
                        .version("v1.0.0"))
                // 3. Aplica a exigência do token globalmente para todas as rotas documentadas
                .addSecurityItem(new SecurityRequirement().addList("bearer-key"));
    }
}
