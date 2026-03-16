package com.FelipeLohan.ecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados de uma categoria")
public class CategoryDTO {

    @Schema(description = "ID da categoria", example = "1")
    private Long id;

    @Schema(description = "Nome da categoria", example = "Eletrônicos")
    private String name;

    @Schema(description = "Indica se a categoria está em destaque", example = "true")
    private Boolean isFeatured;

    public CategoryDTO() {
    }

    public CategoryDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(Boolean isFeatured) {
        this.isFeatured = isFeatured;
    }
}
