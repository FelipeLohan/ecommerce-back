package com.FelipeLohan.ecommerce.services.interfaces;

import java.util.List;

import com.FelipeLohan.ecommerce.dto.CategoryDTO;

public interface CategoryService {

    List<CategoryDTO> findAll();

    List<CategoryDTO> findFeatured();

    CategoryDTO insert(CategoryDTO dto);

    CategoryDTO update(Long id, CategoryDTO dto);

    void delete(Long id);
}
