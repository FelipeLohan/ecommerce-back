package com.FelipeLohan.ecommerce.dto;

import com.FelipeLohan.ecommerce.entities.Category;

public class CategoryDTO {

    private Long id;
    private String name;
    private Boolean isFeatured;

    public CategoryDTO() {
    }

    public CategoryDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public CategoryDTO(Category entity) {
        id = entity.getId();
        name = entity.getName();
        isFeatured = entity.getIsFeatured();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Boolean getIsFeatured() {
        return isFeatured;
    }
}
