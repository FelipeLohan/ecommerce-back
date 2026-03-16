# Plano de Migração para MapStruct

## Objetivo

Substituir as conversões manuais (construtores DTO e métodos `copyDtoToEntity`) por mappers MapStruct, centralizando e padronizando as conversões entidade ↔ DTO em todo o projeto.

## Estado atual

### Padrão de conversão hoje
- **Entidade → DTO:** Construtores nos DTOs recebem a entidade e setam os campos manualmente
- **DTO → Entidade:** Métodos privados `copyDtoToEntity` nos services fazem setters campo a campo
- **Nenhum mapper existe ainda** — MapStruct 1.6.3 está declarado no `pom.xml` com o annotation processor configurado

---

## Etapas de implementação

### Etapa 1 — Criar o pacote e a interface base

Criar pacote `com.FelipeLohan.ecommerce.mappers`.

Criar interface base opcional com configuração padrão (componentModel = "spring").

---

### Etapa 2 — CategoryMapper

**Arquivo:** `mappers/CategoryMapper.java`

**Mapeamentos necessários:**
- `Category → CategoryDTO` (campos diretos: `id`, `name`, `isFeatured`)
- `CategoryDTO → Category` (mesmos campos; ignorar `products` que é gerenciado pelo JPA)

**Atenção:** `isFeatured` pode ser nulo no DTO. Manter a lógica atual:
```java
entity.setIsFeatured(dto.getIsFeatured() != null && dto.getIsFeatured());
```
Isso exigirá um método `@AfterMapping` ou expressão no mapper.

**Services afetados:** `CategoryService`
- Substituir `new CategoryDTO(entity)` por `categoryMapper.toDTO(entity)`
- Substituir setters manuais no insert/update por `categoryMapper.toEntity(dto)`
- Remover método `toCategoryDTO(CategoryRedis)` do service — converter direto ou criar método específico no mapper se necessário

---

### Etapa 3 — ProductMapper

**Arquivo:** `mappers/ProductMapper.java`

**Mapeamentos necessários:**
- `Product → ProductDTO` (inclui `List<CategoryDTO> categories` — mapeamento de coleção via `CategoryMapper`)
- `Product → ProductMinDTO` (subconjunto de campos: `id`, `name`, `price`, `imgUrl`, `isFeatured`)
- `ProductDTO → Product` (campos simples; **ignorar** `categories` e `items` — gerenciados separadamente)

**Atenção:**
- O campo `categories` em `ProductDTO` é uma `List<CategoryDTO>`, mas em `Product` é `Set<Category>`. Precisará de mapeamento explícito ou uso do `CategoryMapper`.
- A coleção `items` (`Set<OrderItem>`) na entidade **deve ser ignorada** no mapeamento DTO → entidade.

**Services afetados:** `ProductService`
- Substituir `new ProductDTO(product)` e `new ProductMinDTO(entity)` pelos métodos do mapper
- Remover método `copyDtoToEntity` — o mapper assumirá o papel; a parte de categorias (busca no repositório) permanece no service

---

### Etapa 4 — UserMapper

**Arquivo:** `mappers/UserMapper.java`

**Mapeamentos necessários:**
- `User → UserDTO` (campos: `id`, `name`, `email`, `phone`, `birthDate`; `roles` deve ser convertida de `Set<Role>` para `List<String>` usando `role.getAuthority()`)
- `UserInsertDTO → User` (campos: `name`, `email`, `phone`, `birthDate`; **ignorar** `password` — encoding com BCrypt deve continuar no service)

**Atenção:**
- `roles` em `UserDTO` é `List<String>` e em `User` é `Set<Role>`. Requer `@Mapping` com expressão ou método auxiliar.
- `password` **nunca deve ser mapeado automaticamente** — o service aplica `passwordEncoder.encode(...)` manualmente.

**Services afetados:** `UserService`
- Substituir `new UserDTO(entity)` por `userMapper.toDTO(entity)`
- Substituir setters manuais por `userMapper.toEntity(dto)` + set manual apenas do `password` encodado

---

### Etapa 5 — OrderMapper

**Arquivo:** `mappers/OrderMapper.java`

**Mapeamentos necessários:**
- `Order → OrderDTO`:
  - `client` (`User`) → `ClientDTO` (campos: `id`, `name`)
  - `payment` (`Payment`) → `PaymentDTO` (campos: `id`, `moment`)
  - `items` (`Set<OrderItem>`) → `List<OrderItemDTO>`
- `OrderItem → OrderItemDTO`:
  - `productId` vem de `item.getId().getProduct().getId()`
  - `name` vem de `item.getId().getProduct().getName()`
  - `imgUrl` vem de `item.getId().getProduct().getImgUrl()`
  - `price` e `quantity` são campos diretos de `OrderItem`

**Atenção:**
- `OrderItemPK` é uma chave composta (`@EmbeddedId`) — os mapeamentos de `productId`, `name` e `imgUrl` precisam de `@Mapping(source = "id.product.X", target = "X")`.
- A criação de `OrderItem` no insert (a partir de `OrderItemDTO`) **permanece manual no service** pois envolve busca no repositório e lógica de negócio.

**Services afetados:** `OrderService`
- Substituir `new OrderDTO(order)` por `orderMapper.toDTO(order)`

---

### Etapa 6 — Remover construtores de mapeamento dos DTOs

Após todos os mappers estarem funcionando e os services atualizados, remover os construtores que recebem entidades dos DTOs:
- `CategoryDTO(Category entity)`
- `ProductDTO(Product entity)`
- `ProductMinDTO(Product entity)`
- `OrderDTO(Order order)`
- `UserDTO(User user)`
- `ClientDTO(User user)` (se existir)
- `PaymentDTO(Payment payment)` (se existir)
- `OrderItemDTO(OrderItem item)` (se existir)

Manter apenas construtores de campo direto (ex: `new CategoryDTO(id, name)` usados em testes ou casos específicos).

---

### Etapa 7 — Validação e testes

- Rodar `./mvnw test` para garantir que nenhuma regressão foi introduzida
- Verificar se os testes de integração/unitários cobrem os services alterados
- Confirmar que a geração do código MapStruct ocorre corretamente com `./mvnw clean package -DskipTests`

---

## Mapa de dependências entre mappers

```
OrderMapper
  └── usa ClientMapper (User → ClientDTO)  ← pode ser inline
  └── usa PaymentMapper (Payment → PaymentDTO) ← pode ser inline
  └── usa OrderItemMapper (OrderItem → OrderItemDTO)

ProductMapper
  └── usa CategoryMapper (Category → Ca.
  tegoryDTO)
```

Use `@Mapper(uses = {CategoryMapper.class})` para injetar mappers dependentes.

---

## Ordem de implementação recomendada

1. `CategoryMapper` (sem dependências externas)
2. `UserMapper` (sem dependências externas)
3. `ProductMapper` (depende de `CategoryMapper`)
4. `OrderMapper` (depende de `UserMapper` — ClientDTO; e OrderItemMapper inline)
5. Limpeza dos construtores de entidade nos DTOs
6. Rodar testes

---

## Observações finais

- **Não usar `@Mapper(componentModel = "spring")` em mappers que precisam de repositórios** — a lógica que busca entidades do banco (ex: `categoryRepository.getReferenceById`) permanece no service.
- **Manter a senha fora do mapeamento automático** — risco de segurança se o password for exposto em algum DTO de resposta.
- O mapper de `OrderHistoryResponseDTO` (MongoDB) é mapeado diretamente do documento `OrderHistoryDocument` — avaliar se um mapper específico é necessário ou se o mapeamento atual no service é suficiente.
