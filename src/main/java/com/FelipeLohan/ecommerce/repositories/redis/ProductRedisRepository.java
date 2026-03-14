package com.FelipeLohan.ecommerce.repositories.redis;

import com.FelipeLohan.ecommerce.entities.redis.ProductRedis;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRedisRepository extends CrudRepository<ProductRedis, Long> {

    List<ProductRedis> findByIsFeatured(Boolean isFeatured);
}
