package com.FelipeLohan.ecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Detalhe de erro em um campo específico")
public class FieldMessage {

    @Schema(description = "Nome do campo com erro", example = "email")
    private String fieldName;

    @Schema(description = "Mensagem de erro do campo", example = "Email inválido")
    private String message;

    public FieldMessage(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getMessage() {
        return message;
    }
}
