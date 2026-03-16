package com.FelipeLohan.ecommerce.controllers.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.FelipeLohan.ecommerce.dto.OrderDTO;

@Tag(name = "Orders", description = "Gerenciamento de pedidos")
@RequestMapping(value = "/orders")
public interface OrderController {

    @Operation(summary = "Buscar pedido por ID", description = "Requer ROLE_CLIENT (próprio pedido) ou ROLE_ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "401", description = "Token ausente ou inválido", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content(schema = @Schema(ref = "#/components/schemas/CustomError")))
    })
    @GetMapping(value = "/{id}")
    ResponseEntity<OrderDTO> findById(
            @Parameter(description = "ID do pedido", required = true) @PathVariable Long id);

    @Operation(summary = "Criar pedido", description = "Cria um novo pedido para o usuário autenticado. Requer ROLE_CLIENT")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Token ausente ou inválido", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado — apenas ROLE_CLIENT", content = @Content),
            @ApiResponse(responseCode = "422", description = "Dados de entrada inválidos (ex.: lista de itens vazia)", content = @Content(schema = @Schema(ref = "#/components/schemas/ValidationError")))
    })
    @PostMapping
    ResponseEntity<OrderDTO> insert(@Valid @RequestBody OrderDTO dto);
}
