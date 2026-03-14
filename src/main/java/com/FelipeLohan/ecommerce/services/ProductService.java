package com.FelipeLohan.ecommerce.services;

import java.util.List;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
import com.FelipeLohan.ecommerce.repositories.ProductRepository;
import com.FelipeLohan.ecommerce.services.exceptions.DatabaseException;
import com.FelipeLohan.ecommerce.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product product = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado"));
        return new ProductDTO(product);
    }

    @Cacheable("featuredProducts")
    @Transactional(readOnly = true)
    public List<ProductMinDTO> findFeatured() {
        List<Product> result = repository.findByIsFeaturedTrue();
        return result.stream().map(x -> new ProductMinDTO(x)).toList();
    }

    @Transactional(readOnly = true)
    public Page<ProductMinDTO> findAll(String name, Long categoryId, Pageable pageable) {
        Long catId = (categoryId == 0) ? null : categoryId;
        Page<Product> result = repository.searchByNameAndCategory(name, catId, pageable);
        return result.map(x -> new ProductMinDTO(x));
    }

    @CacheEvict(value = "featuredProducts", allEntries = true)
    @Transactional
    public ProductDTO insert(ProductDTO dto) {
        Product entity = new Product();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new ProductDTO(entity);
    }

    @CacheEvict(value = "featuredProducts", allEntries = true)
    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        try {
            Product entity = repository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);
            return new ProductDTO(entity);
        }
        catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
    }

    @CacheEvict(value = "featuredProducts", allEntries = true)
    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        try {
            repository.deleteById(id);
        }
        catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial");
        }
    }

    private void copyDtoToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImgUrl(dto.getImgUrl());
        if (dto.getIsFeatured() != null) {
            entity.setIsFeatured(dto.getIsFeatured());
        }

        entity.getCategories().clear();
        for (CategoryDTO catDto : dto.getCategories()) {
            Category cat = new Category();
            cat.setId(catDto.getId());
            entity.getCategories().add(cat);
        }
    }
}
