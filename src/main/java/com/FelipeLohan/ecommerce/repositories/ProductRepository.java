package com.FelipeLohan.ecommerce.repositories;

import com.FelipeLohan.ecommerce.entities.Product;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByIsFeaturedTrue();

    @Query("SELECT DISTINCT obj FROM Product obj " +
            "JOIN obj.categories cat " +
            "WHERE (:categoryId IS NULL OR cat.id = :categoryId) " +
            "AND UPPER(obj.name) LIKE UPPER(CONCAT('%', :name, '%'))")
    Page<Product> searchByNameAndCategory(
            @Param("name") String name,
            @Param("categoryId") Long categoryId,
            Pageable pageable);
}
