package com.FelipeLohan.ecommerce.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para criação de novo usuário")
public class UserInsertDTO {

    @Schema(description = "Nome completo do usuário", example = "João Silva")
    @NotBlank(message = "Campo obrigatório")
    private String name;

    @Schema(description = "E-mail do usuário (deve ser único)", example = "joao@email.com")
    @NotBlank(message = "Campo obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @Schema(description = "Telefone do usuário", example = "+55 11 99999-9999")
    private String phone;

    @Schema(description = "Data de nascimento (formato YYYY-MM-DD)", example = "1990-05-20")
    private LocalDate birthDate;

    @Schema(description = "Senha de acesso (mínimo 6 caracteres)", example = "senha123", minLength = 6)
    @NotBlank(message = "Campo obrigatório")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
