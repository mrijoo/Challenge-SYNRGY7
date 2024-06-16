package com.ch.binarfud.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ch.binarfud.model.Merchant;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, UUID> {
    @Query(value = "SELECT * FROM merchants WHERE open = ?1", nativeQuery = true)
    List<Merchant> findByIsOpen(boolean status);
}
