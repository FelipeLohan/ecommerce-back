package com.FelipeLohan.ecommerce.controllers.interfaces;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.FelipeLohan.ecommerce.dto.CategoryDTO;

@RequestMapping(value = "/categories")
public interface CategoryController {

    @GetMapping
    ResponseEntity<List<CategoryDTO>> findAll();

    @GetMapping("/featured")
    ResponseEntity<List<CategoryDTO>> findFeatured();

    @PostMapping
    ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO dto);

    @PutMapping("/{id}")
    ResponseEntity<CategoryDTO> update(@PathVariable Long id, @RequestBody CategoryDTO dto);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id);
}
