package com.ch.binarfud.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ch.binarfud.model.Product;

public interface ProductService {
    List<Product> getAllProducts();

    ResponseEntity<Product> createProduct(Product product);
}
