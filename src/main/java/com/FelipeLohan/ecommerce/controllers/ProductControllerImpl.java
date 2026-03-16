package com.FelipeLohan.ecommerce.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.FelipeLohan.ecommerce.controllers.interfaces.ProductController;
import com.FelipeLohan.ecommerce.dto.ProductDTO;
import com.FelipeLohan.ecommerce.dto.ProductMinDTO;
import com.FelipeLohan.ecommerce.services.interfaces.ProductService;

@RestController
public class ProductControllerImpl implements ProductController {

    @Autowired
    private ProductService service;

    @Override
    public ResponseEntity<List<ProductMinDTO>> findFeatured() {
        List<ProductMinDTO> list = service.findFeatured();
        return ResponseEntity.ok(list);
    }

    @Override
    public ResponseEntity<ProductDTO> findById(Long id) {
        ProductDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<Page<ProductMinDTO>> findAll(String name, Long categoryId, Pageable pageable) {
        Page<ProductMinDTO> dto = service.findAll(name, categoryId, pageable);
        return ResponseEntity.ok(dto);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ProductDTO> insert(ProductDTO dto) {
        dto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ProductDTO> update(Long id, ProductDTO dto) {
        dto = service.update(id, dto);
        return ResponseEntity.ok(dto);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
