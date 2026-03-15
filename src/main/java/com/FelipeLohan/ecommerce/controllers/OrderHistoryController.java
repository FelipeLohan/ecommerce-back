package com.FelipeLohan.ecommerce.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.FelipeLohan.ecommerce.dto.OrderHistoryResponseDTO;
import com.FelipeLohan.ecommerce.services.OrderHistoryService;

@RestController
@RequestMapping(value = "/order-history")
public class OrderHistoryController {

    @Autowired
    private OrderHistoryService service;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<OrderHistoryResponseDTO>> findAll(
            @RequestParam(required = false) String email,
            @PageableDefault(size = 10, sort = "moment", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<OrderHistoryResponseDTO> page = (email != null && !email.isBlank())
                ? service.findByClientEmail(email, pageable)
                : service.findAll(pageable);

        return ResponseEntity.ok(page);
    }
}
