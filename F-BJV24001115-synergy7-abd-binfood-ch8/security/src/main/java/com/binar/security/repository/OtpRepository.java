package com.binar.security.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.binar.security.enums.OtpType;
import com.binar.security.model.Otp;
import com.binar.security.model.User;

@Repository
public interface OtpRepository extends JpaRepository<Otp, UUID> {
    Otp findByCode(String code);

    Optional<Otp> findByUser(User user);

    Otp findByCodeAndType(String code, OtpType type);
}
