package com.binar.security.dto.validation;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.binar.security.model.User;
import com.binar.security.repository.UserRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class UniqueValidator implements ConstraintValidator<Unique, String> {
    private String column;
    private final UserRepository userRepository;

    public UniqueValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void initialize(Unique constraintAnnotation) {
        this.column = constraintAnnotation.column();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Optional<User> user = Optional.empty();
        if (column.equals("username")) {
            user = userRepository.findByUsername(value);
        } else if (column.equals("email")) {
            user = userRepository.findByEmail(value);
        }
        return user.isEmpty();
    }
}
