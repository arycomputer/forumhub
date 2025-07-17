package br.com.desafioalura.forumhub.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth"; // Nome do esquema de segurança

        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName)) // Aplica o esquema de segurança globalmente
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT") // Formato do token
                                .description("Forneça o token JWT no formato 'Bearer {token}'") // Descrição para o utilizador
                        )
                )
                .info(new Info()
                        .title("API do Fórum de Discussão")
                        .version("1.0")
                        .description("Documentação da API para o aplicativo de fórum de discussão com autenticação JWT.")
                );
    }
}