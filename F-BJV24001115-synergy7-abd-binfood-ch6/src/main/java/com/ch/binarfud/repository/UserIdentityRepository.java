package com.ch.binarfud.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ch.binarfud.model.UserIdentity;

@Repository
public interface UserIdentityRepository extends JpaRepository<UserIdentity, UUID> {
    Page<UserIdentity> findByVerified(boolean b, PageRequest of);
}
