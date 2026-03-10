# CLAUDE.md

## Projeto

Backend de e-commerce em **Java 17 + Spring Boot 2.7.3** com Maven. API REST com autenticação OAuth2/JWT.

## Comandos essenciais

```bash
# Subir banco de dados local
docker-compose up -d

# Rodar a aplicação (perfil dev, PostgreSQL porta 5433)
./mvnw spring-boot:run

# Rodar testes (usa H2 em memória automaticamente)
./mvnw test

# Build
./mvnw clean package -DskipTests
```

## Estrutura de pacotes

```
com.FelipeLohan.ecommerce/
├── config/       # AuthorizationServerConfig, ResourceServerConfig, WebSecurityConfig
├── controllers/  # CategoryController, OrderController, ProductController, UserController
│   └── handlers/ # ControllerExceptionHandler
├── dto/          # DTOs de request/response
├── entities/     # Entidades JPA (User, Product, Order, OrderItem, Category, Payment, Role)
├── repositories/ # Interfaces Spring Data JPA
└── services/     # Lógica de negócio; exceptions/ para exceções customizadas
```

## Convenções do projeto

- **Nomenclatura:** PascalCase para classes, camelCase para métodos/variáveis. Pacote base: `com.FelipeLohan.ecommerce`
- **DTOs:** Classes separadas para input/output. `ProductMinDTO` para listagens, `ProductDTO` para detalhes. `UserInsertDTO` para criação de usuário (estende `UserDTO` com campo de senha)
- **Serviços:** Toda lógica de negócio fica nos services. Controllers apenas delegam chamadas
- **Exceções:** Usar as classes em `services/exceptions/`. `ControllerExceptionHandler` trata e mapeia para HTTP status adequado
- **Segurança:** Rotas públicas configuradas em `ResourceServerConfig`. Acesso granular via `@PreAuthorize` nos serviços

## Banco de dados

- **Dev/Prod:** PostgreSQL
- **Testes:** H2 em memória (perfil `test` ativado automaticamente pelo Maven Surefire)
- **Docker:** `docker-compose up -d` sobe PostgreSQL na porta `5433` e pgAdmin na `5050`
- **DDL dev:** `spring.jpa.hibernate.ddl-auto=update` — Hibernate atualiza o schema automaticamente
- **Seed data:** Definido nos arquivos `application-dev.properties` via `spring.sql.init` ou direto no `create.sql`

## Segurança e autenticação

- OAuth2 Authorization Server com JWT
- `WebSecurityConfig`: configura BCryptPasswordEncoder e carrega UserDetailsService
- `AuthorizationServerConfig`: configura o endpoint `/oauth/token`
- `ResourceServerConfig`: define rotas públicas e CORS
- Roles: `ROLE_CLIENT` e `ROLE_ADMIN`
- Credenciais OAuth2 padrão (dev): `myclientid` / `myclientsecret`

## Perfis de configuração

| Arquivo | Perfil | Uso |
|---------|--------|-----|
| `application.properties` | — | Configurações comuns |
| `application-dev.properties` | `dev` | PostgreSQL local |
| `application-test.properties` | `test` | H2 em memória |
| `application-prod.properties` | `prod` | DATABASE_URL via env var |

## Variáveis de ambiente relevantes

```
APP_PROFILE, CLIENT_ID, CLIENT_SECRET, JWT_SECRET, JWT_DURATION, CORS_ORIGINS, DATABASE_URL
```

## Padrões de erro HTTP

| Exceção | HTTP |
|---------|------|
| `ResourceNotFoundException` | 404 |
| `DatabaseException` | 400 |
| `EmailAlreadyExistsException` | 409 |
| `ForbiddenException` | 403 |
| Erros de validação (`@Valid`) | 422 |
