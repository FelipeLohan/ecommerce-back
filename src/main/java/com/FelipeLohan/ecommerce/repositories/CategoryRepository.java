package com.FelipeLohan.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.FelipeLohan.ecommerce.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
