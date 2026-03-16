# Plano: Interfaces para Services e Controllers

## Objetivo

Criar interfaces para todas as camadas de Service e Controller, implementando o princípio de inversão de dependência (DIP). Cada implementação atual será renomeada para `*Impl` e as injeções de dependência passarão a referenciar as interfaces.

## Convenção adotada

- Interface: nome original (ex: `UserService`)
- Implementação: sufixo `Impl` (ex: `UserServiceImpl`)
- Pacote de interfaces de controller: `controllers` (mesmo pacote, a interface fica junto)
- As anotações Spring (`@Service`, `@RestController`, `@PreAuthorize`, etc.) ficam **nas implementações**, não nas interfaces

---

## Tarefas

### Fase 1 — Interfaces de Service

- [x] **1.1** Criar `UserService` (interface) em `services/interfaces/`
  - Métodos públicos: `loadUserByUsername`, `authenticated`, `getMe`, `register`
  - A interface deve estender `UserDetailsService` (Spring Security)
  - Renomear classe atual para `UserServiceImpl`

- [x] **1.2** Criar `ProductService` (interface) em `services/interfaces/`
  - Métodos: `findById`, `findFeatured`, `findAll`, `insert`, `update`, `delete`
  - Renomear classe atual para `ProductServiceImpl`

- [x] **1.3** Criar `CategoryService` (interface) em `services/interfaces/`
  - Métodos: `findAll`, `findFeatured`, `insert`, `update`, `delete`
  - Renomear classe atual para `CategoryServiceImpl`

- [x] **1.4** Criar `OrderService` (interface) em `services/interfaces/`
  - Métodos: `findById`, `insert`
  - Renomear classe atual para `OrderServiceImpl`

- [x] **1.5** Criar `OrderHistoryService` (interface) em `services/interfaces/`
  - Métodos: `saveHistory`, `findAll`, `findByClientEmail`
  - Renomear classe atual para `OrderHistoryServiceImpl`

- [x] **1.6** Criar `AuthService` (interface) em `services/interfaces/`
  - Métodos: `validateSelfOrAdmin`
  - Renomear classe atual para `AuthServiceImpl`

### Fase 2 — Interfaces de Controller

- [x] **2.1** Criar `UserController` (interface) em `controllers/interfaces/`
  - Métodos: `getMe`, `register`
  - Anotações de mapeamento de rota na interface; `@PreAuthorize` na implementação
  - Renomear classe atual para `UserControllerImpl`

- [x] **2.2** Criar `ProductController` (interface) em `controllers/interfaces/`
  - Métodos: `findFeatured`, `findById`, `findAll`, `insert`, `update`, `delete`
  - Renomear classe atual para `ProductControllerImpl`

- [x] **2.3** Criar `CategoryController` (interface) em `controllers/interfaces/`
  - Métodos: `findAll`, `findFeatured`, `insert`, `update`, `delete`
  - Renomear classe atual para `CategoryControllerImpl`

- [x] **2.4** Criar `OrderController` (interface) em `controllers/interfaces/`
  - Métodos: `findById`, `insert`
  - Renomear classe atual para `OrderControllerImpl`

- [x] **2.5** Criar `OrderHistoryController` (interface) em `controllers/interfaces/`
  - Métodos: `findAll`
  - Renomear classe atual para `OrderHistoryControllerImpl`

### Fase 3 — Atualizar injeções de dependência

- [x] **3.1** Em cada `*ControllerImpl`, injetar pelo tipo da interface de service
- [x] **3.2** Em `OrderServiceImpl`, `AuthService` e `OrderHistoryService` injetados como interfaces
- [x] **3.3** Em `AuthServiceImpl`, `UserService` injetado como interface

### Fase 4 — Verificação

- [x] **4.1** `./mvnw test` — BUILD SUCCESS (1 test, 0 failures)
- [ ] **4.2** Rodar `./mvnw spring-boot:run` e verificar startup sem erros
- [x] **4.3** Spring resolve corretamente os beans via interface (log: `Global AuthenticationManager configured with UserDetailsService bean with name userServiceImpl`)

---

## Decisões de design

| Decisão | Justificativa |
|---|---|
| Anotações de rota na interface do controller | Permite documentar o contrato HTTP sem depender da implementação; Spring MVC herda as anotações |
| `@PreAuthorize` fica na implementação | Evita acoplamento de segurança ao contrato; facilita testes sem contexto de segurança |
| Métodos privados helper ficam apenas na `*Impl` | Helpers internos não fazem parte do contrato público |
| Interfaces no mesmo pacote das implementações | Mantém a estrutura simples sem criar subpacotes `impl/` desnecessários |

---

## Arquivos afetados

### Novos arquivos (interfaces)
- `services/UserService.java`
- `services/ProductService.java`
- `services/CategoryService.java`
- `services/OrderService.java`
- `services/OrderHistoryService.java`
- `services/AuthService.java`
- `controllers/UserController.java`
- `controllers/ProductController.java`
- `controllers/CategoryController.java`
- `controllers/OrderController.java`
- `controllers/OrderHistoryController.java`

### Arquivos renomeados (implementações)
- `UserService.java` → `UserServiceImpl.java`
- `ProductService.java` → `ProductServiceImpl.java`
- `CategoryService.java` → `CategoryServiceImpl.java`
- `OrderService.java` → `OrderServiceImpl.java`
- `OrderHistoryService.java` → `OrderHistoryServiceImpl.java`
- `AuthService.java` → `AuthServiceImpl.java`
- `UserController.java` → `UserControllerImpl.java`
- `ProductController.java` → `ProductControllerImpl.java`
- `CategoryController.java` → `CategoryControllerImpl.java`
- `OrderController.java` → `OrderControllerImpl.java`
- `OrderHistoryController.java` → `OrderHistoryControllerImpl.java`
