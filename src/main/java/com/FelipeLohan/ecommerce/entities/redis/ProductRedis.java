package com.FelipeLohan.ecommerce.entities.redis;

import com.FelipeLohan.ecommerce.entities.Product;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@RedisHash(value = "product", timeToLive = 600)
public class ProductRedis implements Serializable {

    @Id
    private Long id;
    private String name;
    private Double price;
    private String imgUrl;

    @Indexed
    private Boolean isFeatured;

    public ProductRedis() {
    }

    public ProductRedis(Long id, String name, Double price, String imgUrl, Boolean isFeatured) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imgUrl = imgUrl;
        this.isFeatured = isFeatured;
    }

    public static ProductRedis from(Product product) {
        return new ProductRedis(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImgUrl(),
                product.getIsFeatured()
        );
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Boolean getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(Boolean isFeatured) {
        this.isFeatured = isFeatured;
    }
}
