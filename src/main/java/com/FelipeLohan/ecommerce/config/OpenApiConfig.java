package com.FelipeLohan.ecommerce.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "E-commerce API",
                version = "v1",
                description = "API REST para gerenciamento de produtos, categorias, usuários e pedidos. " +
                        "Autenticação via OAuth2 com JWT — obtenha um token em `POST /oauth/token` e use-o no botão Authorize.",
                contact = @Contact(name = "Felipe Lohan", url = "https://github.com/FelipeLohan")
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Servidor local (dev)"),
                @Server(url = "https://ecommerce.felipelohan.com", description = "Servidor de produção")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Token JWT obtido via POST /oauth/token. Formato: Bearer {token}"
)
public class OpenApiConfig {
}
