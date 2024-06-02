package com.ch.binarfud.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import com.ch.binarfud.model.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    @Query(value = "SELECT * FROM users WHERE id = ?1", nativeQuery = true)
    Optional<User> findUserById(UUID id);
    
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Page<User> findAll(Pageable pageable);

    @Procedure(procedureName = "add_user")
    void addUser(String username, String email, String password);

    @Procedure(procedureName = "update_user")
    void updateUser(UUID id, String username, String email, String password);

    @Procedure(procedureName = "delete_user_by_id")
    void deleteUserById(UUID id);

    Optional<User> findUserWithMerchantById(UUID id);

    @Procedure(procedureName = "transfer_balance")
    void transferBalance(UUID senderId, UUID receiverId, double amount);
}
