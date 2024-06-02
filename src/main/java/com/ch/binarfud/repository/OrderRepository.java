package com.ch.binarfud.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ch.binarfud.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    Page<Order> findByUserId(UUID userId, Pageable pageable);
}
