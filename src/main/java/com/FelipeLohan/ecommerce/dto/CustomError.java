package com.FelipeLohan.ecommerce.dto;

import java.time.Instant;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Formato padrão de erro da API")
public class CustomError {

    @Schema(description = "Timestamp do erro", example = "2024-01-15T10:30:00Z")
    private Instant timestamp;

    @Schema(description = "Código HTTP do erro", example = "404")
    private Integer status;

    @Schema(description = "Mensagem descritiva do erro", example = "Recurso não encontrado")
    private String error;

    @Schema(description = "Caminho da requisição que gerou o erro", example = "/products/999")
    private String path;

    public CustomError(Instant timestamp, Integer status, String error, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.path = path;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getPath() {
        return path;
    }
}
