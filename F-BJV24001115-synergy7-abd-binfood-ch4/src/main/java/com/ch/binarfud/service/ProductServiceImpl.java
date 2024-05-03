package com.ch.binarfud.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ch.binarfud.model.Merchant;
import com.ch.binarfud.model.Product;
import com.ch.binarfud.model.User;
import com.ch.binarfud.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public ResponseEntity<Product> createProduct(Product product) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = (User) authentication.getPrincipal();

        Merchant merchant = currentUser.getMerchant();

        product.setMerchant(merchant);
        product.setMerchantId(merchant.getId());

        product = productRepository.save(product);

        return ResponseEntity.ok(product);
    }
}
