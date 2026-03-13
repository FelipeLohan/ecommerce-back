package com.FelipeLohan.ecommerce.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.FelipeLohan.ecommerce.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByIsFeaturedTrue();
}
