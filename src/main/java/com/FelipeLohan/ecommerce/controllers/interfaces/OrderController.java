package com.FelipeLohan.ecommerce.controllers.interfaces;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.FelipeLohan.ecommerce.dto.OrderDTO;

@RequestMapping(value = "/orders")
public interface OrderController {

    @GetMapping(value = "/{id}")
    ResponseEntity<OrderDTO> findById(@PathVariable Long id);

    @PostMapping
    ResponseEntity<OrderDTO> insert(@Valid @RequestBody OrderDTO dto);
}
