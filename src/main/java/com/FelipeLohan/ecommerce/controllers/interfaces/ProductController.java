package com.FelipeLohan.ecommerce.controllers.interfaces;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.FelipeLohan.ecommerce.dto.ProductDTO;
import com.FelipeLohan.ecommerce.dto.ProductMinDTO;

@RequestMapping(value = "/products")
public interface ProductController {

    @GetMapping("/featured")
    ResponseEntity<List<ProductMinDTO>> findFeatured();

    @GetMapping(value = "/{id}")
    ResponseEntity<ProductDTO> findById(@PathVariable Long id);

    @GetMapping
    ResponseEntity<Page<ProductMinDTO>> findAll(
            @RequestParam(name = "name", defaultValue = "") String name,
            @RequestParam(name = "categoryId", defaultValue = "0") Long categoryId,
            Pageable pageable);

    @PostMapping
    ResponseEntity<ProductDTO> insert(@Valid @RequestBody ProductDTO dto);

    @PutMapping(value = "/{id}")
    ResponseEntity<ProductDTO> update(@PathVariable Long id, @Valid @RequestBody ProductDTO dto);

    @DeleteMapping(value = "/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id);
}
