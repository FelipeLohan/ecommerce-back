package com.FelipeLohan.ecommerce.dto;

import com.FelipeLohan.ecommerce.entities.Product;

public class ProductMinDTO {

    private Long id;
    private String name;
    private Double price;
    private String imgUrl;
    private Boolean isFeatured;

    public ProductMinDTO(Long id, String name, Double price, String imgUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imgUrl = imgUrl;
    }

    public ProductMinDTO(Product entity) {
        id = entity.getId();
        name = entity.getName();
        price = entity.getPrice();
        imgUrl = entity.getImgUrl();
        isFeatured = entity.getIsFeatured();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public Boolean getIsFeatured() {
        return isFeatured;
    }
}
