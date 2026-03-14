package com.FelipeLohan.ecommerce.entities.redis;

import com.FelipeLohan.ecommerce.entities.Category;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@RedisHash(value = "category", timeToLive = 600)
public class CategoryRedis implements Serializable {

    @Id
    private Long id;
    private String name;

    @Indexed
    private Boolean isFeatured;

    public CategoryRedis() {
    }

    public CategoryRedis(Long id, String name, Boolean isFeatured) {
        this.id = id;
        this.name = name;
        this.isFeatured = isFeatured;
    }

    public static CategoryRedis from(Category category) {
        return new CategoryRedis(
                category.getId(),
                category.getName(),
                category.getIsFeatured()
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

    public Boolean getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(Boolean isFeatured) {
        this.isFeatured = isFeatured;
    }
}
