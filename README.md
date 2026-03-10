# Ecommerce API

API REST para e-commerce desenvolvida com Spring Boot, com autenticação OAuth2/JWT, controle de acesso por papéis e gerenciamento completo de produtos, pedidos e usuários.

## Stack

- **Java 17** + **Spring Boot 2.7.3**
- **Spring Security** com OAuth2 Authorization Server e JWT
- **Spring Data JPA** + **Hibernate**
- **PostgreSQL** (produção/dev) + **H2** (testes)
- **Maven**
- **Docker Compose**

## Requisitos

- Java 17+
- Maven 3.8+
- Docker e Docker Compose (para o banco em desenvolvimento)

## Configuração local

### 1. Subir o banco de dados

```bash
docker-compose up -d
```

Isso inicia:
- **PostgreSQL 16** na porta `5433` (usuário: `postgres`, senha: `1234567`, banco: `ecommerce`)
- **pgAdmin** na porta `5050`

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
| `JWT_SECRET` | `myjwtsecret` | Chave de assinatura JWT |
| `JWT_DURATION` | `86400` | Duração do token em segundos (24h) |
| `CORS_ORIGINS` | `http://localhost:3000,...` | Origens permitidas (separadas por vírgula) |
| `DATABASE_URL` | — | URL do banco em produção |

## Autenticação

A API usa OAuth2 com tokens JWT. Para obter um token:

```bash
curl -X POST http://localhost:8080/oauth/token \
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
| `GET` | `/products` | Público | Lista produtos (paginado, filtrável por nome) |
| `GET` | `/products/{id}` | Público | Busca produto por ID |
| `POST` | `/products` | ADMIN | Cria produto |
| `PUT` | `/products/{id}` | ADMIN | Atualiza produto |
| `DELETE` | `/products/{id}` | ADMIN | Remove produto |

### Categorias
| Método | Rota | Acesso | Descrição |
|--------|------|--------|-----------|
| `GET` | `/categories` | Público | Lista categorias |
| `GET` | `/categories/{id}` | Público | Busca categoria por ID |

### Usuários
| Método | Rota | Acesso | Descrição |
|--------|------|--------|-----------|
| `GET` | `/users/me` | CLIENT, ADMIN | Perfil do usuário logado |
| `POST` | `/users` | Público | Cadastra novo usuário |

### Pedidos
| Método | Rota | Acesso | Descrição |
|--------|------|--------|-----------|
| `GET` | `/orders/{id}` | CLIENT, ADMIN | Busca pedido por ID |
| `POST` | `/orders` | CLIENT | Cria novo pedido |

## Status de pedidos

- `WAITING_PAYMENT` — Aguardando pagamento
- `PAID` — Pago
- `SHIPPED` — Enviado
- `DELIVERED` — Entregue
- `CANCELED` — Cancelado

## Estrutura do projeto

```
src/main/java/com/FelipeLohan/ecommerce/
├── config/          # Configurações de segurança e OAuth2
├── controllers/     # Endpoints REST
│   └── handlers/    # Tratamento centralizado de exceções
├── dto/             # Data Transfer Objects
├── entities/        # Entidades JPA
├── repositories/    # Repositórios Spring Data
└── services/        # Regras de negócio
    └── exceptions/  # Exceções customizadas
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
