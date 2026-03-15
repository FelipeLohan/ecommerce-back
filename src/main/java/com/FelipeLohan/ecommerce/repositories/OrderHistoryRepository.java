package com.FelipeLohan.ecommerce.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.FelipeLohan.ecommerce.documents.OrderHistoryDocument;

@Repository
public interface OrderHistoryRepository extends MongoRepository<OrderHistoryDocument, String> {

    Page<OrderHistoryDocument> findAllByOrderByMomentDesc(Pageable pageable);

    Page<OrderHistoryDocument> findByClientEmailOrderByMomentDesc(String email, Pageable pageable);
}
