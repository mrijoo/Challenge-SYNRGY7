package com.ch.binarfud.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ch.binarfud.model.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, UUID> {
    boolean existsByRefreshTokenId(UUID refreshTokenId);
    Token findByRefreshTokenId(UUID refreshTokenId);
}
