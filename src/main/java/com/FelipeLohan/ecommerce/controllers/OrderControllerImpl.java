package com.FelipeLohan.ecommerce.controllers;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.FelipeLohan.ecommerce.controllers.interfaces.OrderController;
import com.FelipeLohan.ecommerce.dto.OrderDTO;
import com.FelipeLohan.ecommerce.services.interfaces.OrderService;

@RestController
public class OrderControllerImpl implements OrderController {

    private final OrderService service;

    public OrderControllerImpl(OrderService service) {
        this.service = service;
    }

    @Override
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
    public ResponseEntity<OrderDTO> findById(Long id) {
        OrderDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<OrderDTO> insert(OrderDTO dto) {
        dto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }
}
