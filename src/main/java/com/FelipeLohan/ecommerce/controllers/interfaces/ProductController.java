package com.FelipeLohan.ecommerce.controllers.interfaces;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.FelipeLohan.ecommerce.dto.ProductDTO;
import com.FelipeLohan.ecommerce.dto.ProductMinDTO;

@Tag(name = "Products", description = "Gerenciamento de produtos")
@RequestMapping(value = "/products")
public interface ProductController {

    @Operation(summary = "Listar produtos em destaque", description = "Retorna todos os produtos com isFeatured = true")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping("/featured")
    ResponseEntity<List<ProductMinDTO>> findFeatured();

    @Operation(summary = "Buscar produto por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto encontrado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content(schema = @Schema(ref = "#/components/schemas/CustomError")))
    })
    @GetMapping(value = "/{id}")
    ResponseEntity<ProductDTO> findById(
            @Parameter(description = "ID do produto", required = true) @PathVariable Long id);

    @Operation(summary = "Listar produtos paginados", description = "Permite filtrar por nome e categoria com paginação")
    @ApiResponse(responseCode = "200", description = "Página de produtos retornada com sucesso")
    @GetMapping
    ResponseEntity<Page<ProductMinDTO>> findAll(
            @Parameter(description = "Filtrar pelo nome do produto (parcial, case-insensitive)") @RequestParam(name = "name", defaultValue = "") String name,
            @Parameter(description = "Filtrar por ID de categoria (0 para ignorar)") @RequestParam(name = "categoryId", defaultValue = "0") Long categoryId,
            Pageable pageable);

    @Operation(summary = "Criar produto", description = "Requer perfil ROLE_ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado — apenas ROLE_ADMIN", content = @Content),
            @ApiResponse(responseCode = "422", description = "Dados de entrada inválidos", content = @Content(schema = @Schema(ref = "#/components/schemas/ValidationError")))
    })
    @PostMapping
    ResponseEntity<ProductDTO> insert(@Valid @RequestBody ProductDTO dto);

    @Operation(summary = "Atualizar produto", description = "Requer perfil ROLE_ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado — apenas ROLE_ADMIN", content = @Content),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content(schema = @Schema(ref = "#/components/schemas/CustomError"))),
            @ApiResponse(responseCode = "422", description = "Dados de entrada inválidos", content = @Content(schema = @Schema(ref = "#/components/schemas/ValidationError")))
    })
    @PutMapping(value = "/{id}")
    ResponseEntity<ProductDTO> update(
            @Parameter(description = "ID do produto", required = true) @PathVariable Long id,
            @Valid @RequestBody ProductDTO dto);

    @Operation(summary = "Excluir produto", description = "Requer perfil ROLE_ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Produto excluído com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado — apenas ROLE_ADMIN", content = @Content),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content(schema = @Schema(ref = "#/components/schemas/CustomError"))),
            @ApiResponse(responseCode = "400", description = "Produto possui dependências (não pode ser excluído)", content = @Content(schema = @Schema(ref = "#/components/schemas/CustomError")))
    })
    @DeleteMapping(value = "/{id}")
    ResponseEntity<Void> delete(
            @Parameter(description = "ID do produto", required = true) @PathVariable Long id);
}
