package com.binar.security.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.binar.security.model.UserIdentity;

@Repository
public interface UserIdentityRepository extends JpaRepository<UserIdentity, UUID> {
    UserIdentity findByUserId(UUID userId);
}
