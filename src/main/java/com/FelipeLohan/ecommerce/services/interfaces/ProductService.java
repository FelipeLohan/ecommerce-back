package com.FelipeLohan.ecommerce.services.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.FelipeLohan.ecommerce.dto.ProductDTO;
import com.FelipeLohan.ecommerce.dto.ProductMinDTO;

public interface ProductService {

    ProductDTO findById(Long id);

    List<ProductMinDTO> findFeatured();

    Page<ProductMinDTO> findAll(String name, Long categoryId, Pageable pageable);

    ProductDTO insert(ProductDTO dto);

    ProductDTO update(Long id, ProductDTO dto);

    void delete(Long id);
}
