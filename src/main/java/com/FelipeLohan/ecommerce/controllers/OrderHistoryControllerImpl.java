package com.FelipeLohan.ecommerce.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import com.FelipeLohan.ecommerce.controllers.interfaces.OrderHistoryController;
import com.FelipeLohan.ecommerce.dto.OrderHistoryResponseDTO;
import com.FelipeLohan.ecommerce.services.interfaces.OrderHistoryService;

@RestController
public class OrderHistoryControllerImpl implements OrderHistoryController {

    @Autowired
    private OrderHistoryService service;

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<OrderHistoryResponseDTO>> findAll(String email, Pageable pageable) {
        Page<OrderHistoryResponseDTO> page = (email != null && !email.isBlank())
                ? service.findByClientEmail(email, pageable)
                : service.findAll(pageable);

        return ResponseEntity.ok(page);
    }
}
