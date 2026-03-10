package com.FelipeLohan.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.FelipeLohan.ecommerce.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
