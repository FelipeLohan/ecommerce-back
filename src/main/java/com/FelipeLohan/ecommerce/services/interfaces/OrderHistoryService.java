package com.FelipeLohan.ecommerce.services.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.FelipeLohan.ecommerce.dto.OrderDTO;
import com.FelipeLohan.ecommerce.dto.OrderHistoryResponseDTO;

public interface OrderHistoryService {

    void saveHistory(OrderDTO dto, String clientEmail);

    Page<OrderHistoryResponseDTO> findAll(Pageable pageable);

    Page<OrderHistoryResponseDTO> findByClientEmail(String email, Pageable pageable);
}
