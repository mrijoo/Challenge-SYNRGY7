package com.ch.binarfud.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ch.binarfud.model.User;
import com.ch.binarfud.model.Otp;

@Repository
public interface OtpRepository extends JpaRepository<Otp, UUID> {
    Otp findByCode(String code);

    Optional<Otp> findByUser(User user);

    Otp findByCodeAndType(String code, Otp.Type type);
}
