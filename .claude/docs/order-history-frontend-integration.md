# Integração Frontend: Histórico de Pedidos

## Visão geral

O endpoint `/order-history` retorna o histórico de todos os pedidos registrados no sistema, paginado e ordenado do mais recente para o mais antigo. É destinado exclusivamente à tela de administrador.

---

## Autenticação

Todos os requests precisam de um JWT Bearer token no header `Authorization`. O token é obtido no endpoint de login.

### 1. Obter token

**POST** `/oauth/token`

```
Content-Type: application/x-www-form-urlencoded
Authorization: Basic bXljbGllbnRpZDpteWNsaWVudHNlY3JldA==
```

> O valor do header `Authorization` é o Base64 de `myclientid:myclientsecret`
> Em produção, as credenciais OAuth são definidas pelas variáveis de ambiente `CLIENT_ID` e `CLIENT_SECRET`.

Body (form-urlencoded):
```
grant_type=password
username=admin@email.com
password=123456
```

Resposta:
```json
{
  "access_token": "eyJhbGciOiJIUzI1NiJ9...",
  "token_type": "Bearer",
  "expires_in": 86400
}
```

### 2. Usar o token

Em todos os requests subsequentes:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

> O endpoint `/order-history` exige que o usuário autenticado tenha a role `ROLE_ADMIN`. Se o token não tiver essa role, a resposta será `403 Forbidden`.

---

## Endpoint: Histórico de Pedidos

### Listar todos os pedidos

**GET** `/order-history`

```
Authorization: Bearer {token}
```

### Filtrar por e-mail do cliente

**GET** `/order-history?email=cliente@email.com`

```
Authorization: Bearer {token}
```

### Parâmetros de paginação

| Parâmetro | Tipo    | Padrão | Descrição                          |
|-----------|---------|--------|------------------------------------|
| `page`    | integer | `0`    | Número da página (começa em 0)     |
| `size`    | integer | `10`   | Quantidade de itens por página     |
| `email`   | string  | —      | Filtra pedidos de um cliente específico |

Exemplo com paginação:
```
GET /order-history?page=0&size=20
GET /order-history?email=maria@email.com&page=0&size=10
```

---

## Contrato de resposta

```json
{
  "content": [
    {
      "id": "65f1a2b3c4d5e6f7a8b9c0d1",
      "orderId": 42,
      "moment": "2026-03-15T14:30:00Z",
      "status": "PAID",
      "paymentMoment": "2026-03-15T14:32:00Z",
      "clientName": "Maria Silva",
      "clientEmail": "maria@email.com",
      "items": [
        {
          "productId": 7,
          "name": "Smart TV 55\"",
          "price": 2499.90,
          "quantity": 1,
          "imgUrl": "https://...",
          "subTotal": 2499.90
        }
      ],
      "total": 2499.90
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 100,
  "totalPages": 10,
  "last": false,
  "first": true,
  "numberOfElements": 10
}
```

### Campos do objeto de pedido

| Campo           | Tipo            | Pode ser nulo | Descrição                                              |
|-----------------|-----------------|---------------|--------------------------------------------------------|
| `id`            | string          | não           | ID interno do MongoDB (ObjectId)                       |
| `orderId`       | number          | não           | ID do pedido no banco relacional (PostgreSQL)          |
| `moment`        | string (ISO 8601) | não         | Data/hora da criação do pedido                         |
| `status`        | string (enum)   | não           | Status atual do pedido (ver tabela abaixo)             |
| `paymentMoment` | string (ISO 8601) | **sim**     | Data/hora do pagamento. `null` se ainda não foi pago   |
| `clientName`    | string          | não           | Nome do cliente que fez o pedido                       |
| `clientEmail`   | string          | não           | E-mail do cliente                                      |
| `items`         | array           | não           | Lista de itens do pedido                               |
| `total`         | number          | não           | Valor total do pedido (soma dos subTotais dos itens)   |

### Valores possíveis de `status`

| Valor             | Descrição              |
|-------------------|------------------------|
| `WAITING_PAYMENT` | Aguardando pagamento   |
| `PAID`            | Pago                   |
| `SHIPPED`         | Enviado                |
| `DELIVERED`       | Entregue               |
| `CANCELED`        | Cancelado              |

### Campos de cada item (`items[]`)

| Campo       | Tipo   | Descrição                            |
|-------------|--------|--------------------------------------|
| `productId` | number | ID do produto                        |
| `name`      | string | Nome do produto no momento do pedido |
| `price`     | number | Preço unitário                       |
| `quantity`  | number | Quantidade                           |
| `imgUrl`    | string | URL da imagem do produto             |
| `subTotal`  | number | `price × quantity`                   |

---

## Respostas de erro

| Status | Situação                                               |
|--------|--------------------------------------------------------|
| `401`  | Token ausente, expirado ou inválido                    |
| `403`  | Token válido, mas o usuário não tem `ROLE_ADMIN`       |

---

## Observação sobre os dados

O histórico armazena um **snapshot** do pedido no momento em que ele foi criado. Isso significa que os dados de nome do produto, preço e dados do cliente refletem o estado no momento do pedido, independente de alterações posteriores no cadastro.
