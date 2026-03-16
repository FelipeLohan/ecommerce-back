package com.FelipeLohan.ecommerce.controllers.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.FelipeLohan.ecommerce.dto.OrderHistoryResponseDTO;

@RequestMapping(value = "/order-history")
public interface OrderHistoryController {

    @GetMapping
    ResponseEntity<Page<OrderHistoryResponseDTO>> findAll(
            @RequestParam(required = false) String email,
            @PageableDefault(size = 10, sort = "moment", direction = Sort.Direction.DESC) Pageable pageable);
}
