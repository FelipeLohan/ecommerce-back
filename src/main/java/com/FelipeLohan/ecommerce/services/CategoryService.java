package com.FelipeLohan.ecommerce.services;

import java.util.List;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.FelipeLohan.ecommerce.dto.CategoryDTO;
import com.FelipeLohan.ecommerce.entities.Category;
import com.FelipeLohan.ecommerce.repositories.CategoryRepository;
import com.FelipeLohan.ecommerce.services.exceptions.DatabaseException;
import com.FelipeLohan.ecommerce.services.exceptions.ResourceNotFoundException;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        List<Category> result = repository.findAll();
        return result.stream().map(x -> new CategoryDTO(x)).toList();
    }

    @Cacheable("featuredCategories")
    @Transactional(readOnly = true)
    public List<CategoryDTO> findFeatured() {
        List<Category> result = repository.findByIsFeaturedTrue();
        return result.stream().map(x -> new CategoryDTO(x)).toList();
    }

    @CacheEvict(value = "featuredCategories", allEntries = true)
    @Transactional
    public CategoryDTO insert(CategoryDTO dto) {
        Category entity = new Category();
        entity.setName(dto.getName());
        entity.setIsFeatured(dto.getIsFeatured() != null && dto.getIsFeatured());
        entity = repository.save(entity);
        return new CategoryDTO(entity);
    }

    @CacheEvict(value = "featuredCategories", allEntries = true)
    @Transactional
    public CategoryDTO update(Long id, CategoryDTO dto) {
        try {
            Category entity = repository.getReferenceById(id);
            entity.setName(dto.getName());
            if (dto.getIsFeatured() != null) {
                entity.setIsFeatured(dto.getIsFeatured());
            }
            entity = repository.save(entity);
            return new CategoryDTO(entity);
        }
        catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Categoria não encontrada");
        }
    }

    @CacheEvict(value = "featuredCategories", allEntries = true)
    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Categoria não encontrada");
        }
        try {
            repository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Categoria possui produtos vinculados e não pode ser excluída");
        }
    }
}
