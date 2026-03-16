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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.FelipeLohan.ecommerce.dto.CategoryDTO;

@Tag(name = "Categories", description = "Gerenciamento de categorias")
@RequestMapping(value = "/categories")
public interface CategoryController {

    @Operation(summary = "Listar todas as categorias")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    ResponseEntity<List<CategoryDTO>> findAll();

    @Operation(summary = "Listar categorias em destaque", description = "Retorna categorias com isFeatured = true")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping("/featured")
    ResponseEntity<List<CategoryDTO>> findFeatured();

    @Operation(summary = "Criar categoria", description = "Requer perfil ROLE_ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado — apenas ROLE_ADMIN", content = @Content)
    })
    @PostMapping
    ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO dto);

    @Operation(summary = "Atualizar categoria", description = "Requer perfil ROLE_ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado — apenas ROLE_ADMIN", content = @Content),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada", content = @Content(schema = @Schema(ref = "#/components/schemas/CustomError")))
    })
    @PutMapping("/{id}")
    ResponseEntity<CategoryDTO> update(
            @Parameter(description = "ID da categoria", required = true) @PathVariable Long id,
            @RequestBody CategoryDTO dto);

    @Operation(summary = "Excluir categoria", description = "Requer perfil ROLE_ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Categoria excluída com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado — apenas ROLE_ADMIN", content = @Content),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada", content = @Content(schema = @Schema(ref = "#/components/schemas/CustomError"))),
            @ApiResponse(responseCode = "400", description = "Categoria possui produtos vinculados", content = @Content(schema = @Schema(ref = "#/components/schemas/CustomError")))
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(
            @Parameter(description = "ID da categoria", required = true) @PathVariable Long id);
}
