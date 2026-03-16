package com.FelipeLohan.ecommerce.controllers.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.FelipeLohan.ecommerce.dto.OrderHistoryResponseDTO;

@Tag(name = "Order History", description = "Histórico de pedidos — exclusivo para ROLE_ADMIN")
@RequestMapping(value = "/order-history")
public interface OrderHistoryController {

    @Operation(
            summary = "Listar histórico de pedidos",
            description = "Retorna histórico paginado (padrão: 10 por página, ordenado por data decrescente). " +
                    "Permite filtrar pelo e-mail do cliente. Requer ROLE_ADMIN"
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Histórico retornado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Token ausente ou inválido", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado — apenas ROLE_ADMIN", content = @Content)
    })
    @GetMapping
    ResponseEntity<Page<OrderHistoryResponseDTO>> findAll(
            @Parameter(description = "Filtrar pelo e-mail do cliente (opcional)") @RequestParam(required = false) String email,
            @PageableDefault(size = 10, sort = "moment", direction = Sort.Direction.DESC) Pageable pageable);
}
