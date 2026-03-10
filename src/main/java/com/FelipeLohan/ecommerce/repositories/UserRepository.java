package com.FelipeLohan.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.FelipeLohan.ecommerce.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}
