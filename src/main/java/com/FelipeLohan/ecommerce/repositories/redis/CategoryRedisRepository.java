package com.FelipeLohan.ecommerce.repositories.redis;

import com.FelipeLohan.ecommerce.entities.redis.CategoryRedis;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRedisRepository extends CrudRepository<CategoryRedis, Long> {

    List<CategoryRedis> findByIsFeatured(Boolean isFeatured);
}
