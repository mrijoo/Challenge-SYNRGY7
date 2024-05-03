package com.ch.binarfud.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ch.binarfud.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    Iterable<Product> findByMerchantId(UUID id);
}
