package com.FelipeLohan.ecommerce.services.interfaces;

import com.FelipeLohan.ecommerce.dto.OrderDTO;

public interface OrderService {

    OrderDTO findById(Long id);

    OrderDTO insert(OrderDTO dto);
}
