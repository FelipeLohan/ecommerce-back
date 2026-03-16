# Ecommerce API

API REST para e-commerce desenvolvida com Spring Boot, com autenticação OAuth2/JWT, controle de acesso por papéis e gerenciamento completo de produtos, pedidos e usuários.

**Ambiente de produção:** https://ecommerce.felipelohan.com/

**Documentação interativa (Swagger):** `http://localhost:8080/swagger-ui.html`

## Stack

- **Java 17** + **Spring Boot 4.0.2**
- **Spring Security** com OAuth2 Authorization Server e JWT
- **Spring Data JPA** + **Hibernate**
- **Spring Data MongoDB** — histórico de pedidos
- **Spring Data Redis** — cache de produtos e categorias
- **MapStruct** — mapeamento de objetos
- **springdoc-openapi** — documentação OpenAPI/Swagger
- **PostgreSQL** (produção/dev) + **H2** (testes)
- **Maven**
- **Docker Compose**

## Requisitos

- Java 17+
- Maven 3.8+
- Docker e Docker Compose (para os bancos em desenvolvimento)

## Configuração local

### 1. Subir os serviços de infraestrutura

```bash
docker-compose up -d
```

Isso inicia:
- **PostgreSQL 16** na porta `5433` (usuário: `postgres`, senha: `1234567`, banco: `ecommerce`)
- **pgAdmin** na porta `5050`
- **Redis 7** na porta `6379`
- **RedisInsight** na porta `5540`
- **MongoDB 7** na porta `27017` (usuário: `mongo`, senha: `1234567`)
- **Mongo Express** na porta `8081`

### 2. Rodar a aplicação

```bash
./mvnw spring-boot:run
```

O perfil padrão é `dev`, que se conecta ao PostgreSQL local.

A API estará disponível em `http://localhost:8080`.

## Perfis

| Perfil | Banco | Uso |
|--------|-------|-----|
| `dev`  | PostgreSQL local (porta 5433) | Desenvolvimento |
| `test` | H2 em memória | Testes automatizados |
| `prod` | `DATABASE_URL` (variável de ambiente) | Produção |

Altere o perfil via variável de ambiente:
```bash
APP_PROFILE=prod ./mvnw spring-boot:run
```

## Variáveis de ambiente

| Variável | Padrão | Descrição |
|----------|--------|-----------|
| `APP_PROFILE` | `dev` | Perfil ativo |
| `CLIENT_ID` | `myclientid` | ID do cliente OAuth2 |
| `CLIENT_SECRET` | `myclientsecret` | Secret do cliente OAuth2 |
| `JWT_SECRET` | `myjwtsecret` | Chave de assinatura JWT (HS256) |
| `JWT_DURATION` | `86400` | Duração do token em segundos (24h) |
| `CORS_ORIGINS` | `http://localhost:3000,...` | Origens permitidas (separadas por vírgula) |
| `DATABASE_URL` | — | URL do banco PostgreSQL em produção |
| `MONGODB_URL` | — | URI de conexão com MongoDB |
| `REDIS_URL` | — | URL do Redis em produção |

## Autenticação

A API usa OAuth2 com tokens JWT (HMAC-SHA256). Para obter um token:

**Local:**
```bash
curl -X POST http://localhost:8080/oauth/token \
  -u "myclientid:myclientsecret" \
  -d "grant_type=password&username=alex@gmail.com&password=123456"
```

**Produção:**
```bash
curl -X POST https://ecommerce.felipelohan.com/oauth/token \
  -u "myclientid:myclientsecret" \
  -d "grant_type=password&username=alex@gmail.com&password=123456"
```

Use o token retornado no header `Authorization: Bearer <token>`.

### Usuários de teste (seed data)

| Email | Senha | Papel |
|-------|-------|-------|
| `maria@gmail.com` | `123456` | ROLE_CLIENT |
| `alex@gmail.com` | `123456` | ROLE_CLIENT, ROLE_ADMIN |

## Endpoints

### Produtos
| Método | Rota | Acesso | Descrição |
|--------|------|--------|-----------|
| `GET` | `/products` | Público | Lista produtos (paginado, filtrável por nome e categoria) |
| `GET` | `/products/featured` | Público | Lista produtos em destaque |
| `GET` | `/products/{id}` | Público | Busca produto por ID |
| `POST` | `/products` | ADMIN | Cria produto |
| `PUT` | `/products/{id}` | ADMIN | Atualiza produto |
| `DELETE` | `/products/{id}` | ADMIN | Remove produto |

### Categorias
| Método | Rota | Acesso | Descrição |
|--------|------|--------|-----------|
| `GET` | `/categories` | Público | Lista todas as categorias |
| `GET` | `/categories/featured` | Público | Lista categorias em destaque |
| `POST` | `/categories` | ADMIN | Cria categoria |
| `PUT` | `/categories/{id}` | ADMIN | Atualiza categoria |
| `DELETE` | `/categories/{id}` | ADMIN | Remove categoria |

### Usuários
| Método | Rota | Acesso | Descrição |
|--------|------|--------|-----------|
| `GET` | `/users/me` | CLIENT, ADMIN | Perfil do usuário autenticado |
| `POST` | `/users/register` | Público | Cadastra novo usuário |

### Pedidos
| Método | Rota | Acesso | Descrição |
|--------|------|--------|-----------|
| `GET` | `/orders/{id}` | CLIENT (próprio), ADMIN | Busca pedido por ID |
| `POST` | `/orders` | CLIENT | Cria novo pedido |

### Histórico de pedidos
| Método | Rota | Acesso | Descrição |
|--------|------|--------|-----------|
| `GET` | `/order-history` | ADMIN | Lista histórico de pedidos (paginado, filtrável por e-mail) |

## Status de pedidos

- `WAITING_PAYMENT` — Aguardando pagamento
- `PAID` — Pago
- `SHIPPED` — Enviado
- `DELIVERED` — Entregue
- `CANCELED` — Cancelado

## Tratamento de erros

| Exceção | HTTP |
|---------|------|
| `ResourceNotFoundException` | 404 |
| `DatabaseException` | 400 |
| `EmailAlreadyExistsException` | 409 |
| `ForbiddenException` | 403 |
| Erros de validação (`@Valid`) | 422 |

## Estrutura do projeto

```
src/main/java/com/FelipeLohan/ecommerce/
├── config/               # Segurança, OAuth2, OpenAPI, Redis, MongoDB
├── controllers/
│   ├── interfaces/       # Interfaces de controllers com anotações OpenAPI
│   ├── *Impl.java        # Implementações dos controllers
│   └── handlers/         # Tratamento centralizado de exceções
├── documents/            # Documents MongoDB (OrderHistoryDocument)
├── dto/                  # Data Transfer Objects (request/response)
├── entities/             # Entidades JPA
│   └── redis/            # Entidades para cache Redis
├── mappers/              # Mappers MapStruct
├── repositories/         # Repositórios Spring Data (JPA, MongoDB, Redis)
│   └── redis/
└── services/
    ├── interfaces/       # Interfaces dos services
    ├── *Impl.java        # Implementações dos services
    └── exceptions/       # Exceções customizadas
```

## Testes

```bash
./mvnw test
```

Os testes usam o perfil `test` com banco H2 em memória automaticamente.

## Build para produção

```bash
./mvnw clean package -DskipTests
java -jar target/ecommerce-0.0.1-SNAPSHOT.jar
```
