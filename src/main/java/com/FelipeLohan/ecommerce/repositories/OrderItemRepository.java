package com.FelipeLohan.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.FelipeLohan.ecommerce.entities.OrderItem;
import com.FelipeLohan.ecommerce.entities.OrderItemPK;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK> {

}
