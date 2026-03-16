package com.FelipeLohan.ecommerce.services;

import java.util.List;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.FelipeLohan.ecommerce.dto.CategoryDTO;
import com.FelipeLohan.ecommerce.dto.ProductDTO;
import com.FelipeLohan.ecommerce.dto.ProductMinDTO;
import com.FelipeLohan.ecommerce.entities.Category;
import com.FelipeLohan.ecommerce.entities.Product;
import com.FelipeLohan.ecommerce.entities.redis.ProductRedis;
import com.FelipeLohan.ecommerce.mappers.ProductMapper;
import com.FelipeLohan.ecommerce.repositories.CategoryRepository;
import com.FelipeLohan.ecommerce.repositories.ProductRepository;
import com.FelipeLohan.ecommerce.repositories.redis.ProductRedisRepository;
import com.FelipeLohan.ecommerce.services.exceptions.DatabaseException;
import com.FelipeLohan.ecommerce.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRedisRepository productRedisRepository;

    @Autowired
    private ProductMapper productMapper;

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product product = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado"));
        return productMapper.toDTO(product);
    }

    @Transactional(readOnly = true)
    public List<ProductMinDTO> findFeatured() {
        List<ProductRedis> cached = productRedisRepository.findByIsFeatured(true);
        if (!cached.isEmpty()) {
            return cached.stream().map(this::toProductMinDTO).toList();
        }

        List<Product> result = repository.findByIsFeaturedTrue();
        List<ProductRedis> redisEntities = result.stream().map(ProductRedis::from).toList();
        productRedisRepository.saveAll(redisEntities);
        return result.stream().map(productMapper::toMinDTO).toList();
    }

    @Transactional(readOnly = true)
    public Page<ProductMinDTO> findAll(String name, Long categoryId, Pageable pageable) {
        Long catId = (categoryId == 0) ? null : categoryId;
        Page<Product> result = repository.searchByNameAndCategory(name, catId, pageable);
        return result.map(productMapper::toMinDTO);
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto) {
        Product entity = new Product();
        productMapper.updateEntity(dto, entity);
        applyCategories(dto, entity);
        entity = repository.save(entity);
        if (Boolean.TRUE.equals(entity.getIsFeatured())) {
            productRedisRepository.save(ProductRedis.from(entity));
        }
        return productMapper.toDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        try {
            Product entity = repository.getReferenceById(id);
            productMapper.updateEntity(dto, entity);
            applyCategories(dto, entity);
            entity = repository.save(entity);
            productRedisRepository.deleteById(id);
            if (Boolean.TRUE.equals(entity.getIsFeatured())) {
                productRedisRepository.save(ProductRedis.from(entity));
            }
            return productMapper.toDTO(entity);
        }
        catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        try {
            repository.deleteById(id);
            productRedisRepository.deleteById(id);
        }
        catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial");
        }
    }

    private ProductMinDTO toProductMinDTO(ProductRedis p) {
        return new ProductMinDTO(p.getId(), p.getName(), p.getPrice(), p.getImgUrl());
    }

    private void applyCategories(ProductDTO dto, Product entity) {
        entity.getCategories().clear();
        for (CategoryDTO catDto : dto.getCategories()) {
            Category cat = categoryRepository.getReferenceById(catDto.getId());
            entity.getCategories().add(cat);
        }
    }
}
