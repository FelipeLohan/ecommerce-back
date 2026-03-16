package com.FelipeLohan.ecommerce.controllers.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.FelipeLohan.ecommerce.dto.UserDTO;
import com.FelipeLohan.ecommerce.dto.UserInsertDTO;

@Tag(name = "Users", description = "Gerenciamento de usuários")
@RequestMapping(value = "/users")
public interface UserController {

    @Operation(summary = "Obter dados do usuário autenticado", description = "Retorna os dados do usuário logado. Requer ROLE_CLIENT ou ROLE_ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dados do usuário retornados com sucesso"),
            @ApiResponse(responseCode = "401", description = "Token ausente ou inválido", content = @Content)
    })
    @GetMapping(value = "/me")
    ResponseEntity<UserDTO> getMe();

    @Operation(summary = "Registrar novo usuário", description = "Cria uma nova conta com perfil ROLE_CLIENT")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "409", description = "E-mail já cadastrado", content = @Content(schema = @Schema(ref = "#/components/schemas/CustomError"))),
            @ApiResponse(responseCode = "422", description = "Dados de entrada inválidos", content = @Content(schema = @Schema(ref = "#/components/schemas/ValidationError")))
    })
    @PostMapping(value = "/register")
    ResponseEntity<UserDTO> register(@Valid @RequestBody UserInsertDTO dto);
}
