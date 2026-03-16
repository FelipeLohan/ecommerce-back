package com.FelipeLohan.ecommerce.services;

import java.util.List;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.FelipeLohan.ecommerce.dto.CategoryDTO;
import com.FelipeLohan.ecommerce.entities.Category;
import com.FelipeLohan.ecommerce.entities.redis.CategoryRedis;
import com.FelipeLohan.ecommerce.mappers.CategoryMapper;
import com.FelipeLohan.ecommerce.repositories.CategoryRepository;
import com.FelipeLohan.ecommerce.repositories.redis.CategoryRedisRepository;
import com.FelipeLohan.ecommerce.services.exceptions.DatabaseException;
import com.FelipeLohan.ecommerce.services.exceptions.ResourceNotFoundException;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Autowired
    private CategoryRedisRepository categoryRedisRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        List<Category> result = repository.findAll();
        return result.stream().map(categoryMapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<CategoryDTO> findFeatured() {
        List<CategoryRedis> cached = categoryRedisRepository.findByIsFeatured(true);
        if (!cached.isEmpty()) {
            return cached.stream().map(this::toCategoryDTO).toList();
        }

        List<Category> result = repository.findByIsFeaturedTrue();
        List<CategoryRedis> redisEntities = result.stream().map(CategoryRedis::from).toList();
        categoryRedisRepository.saveAll(redisEntities);
        return result.stream().map(categoryMapper::toDTO).toList();
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO dto) {
        Category entity = new Category();
        categoryMapper.updateEntity(dto, entity);
        entity = repository.save(entity);
        if (Boolean.TRUE.equals(entity.getIsFeatured())) {
            categoryRedisRepository.save(CategoryRedis.from(entity));
        }
        return categoryMapper.toDTO(entity);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO dto) {
        try {
            Category entity = repository.getReferenceById(id);
            categoryMapper.updateEntity(dto, entity);
            entity = repository.save(entity);
            categoryRedisRepository.deleteById(id);
            if (Boolean.TRUE.equals(entity.getIsFeatured())) {
                categoryRedisRepository.save(CategoryRedis.from(entity));
            }
            return categoryMapper.toDTO(entity);
        }
        catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Categoria não encontrada");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Categoria não encontrada");
        }
        try {
            repository.deleteById(id);
            categoryRedisRepository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Categoria possui produtos vinculados e não pode ser excluída");
        }
    }

    private CategoryDTO toCategoryDTO(CategoryRedis c) {
        return new CategoryDTO(c.getId(), c.getName());
    }
}
