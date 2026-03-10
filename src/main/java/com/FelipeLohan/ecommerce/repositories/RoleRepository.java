package com.FelipeLohan.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.FelipeLohan.ecommerce.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByAuthority(String authority);
}
