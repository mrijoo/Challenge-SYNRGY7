package com.ch.binarfud.service;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.ch.binarfud.dto.product.request.CreateProductDto;
import com.ch.binarfud.dto.product.request.UpdateProductDto;
import com.ch.binarfud.dto.product.response.AllProductResponseDto;
import com.ch.binarfud.model.Merchant;
import com.ch.binarfud.model.Product;

public interface ProductService {
    Page<AllProductResponseDto> getAllProduct(int page, int size);

    Product getProductById(UUID id);

    Product addProduct(Merchant merchant, CreateProductDto createProductDto);

    Product updateProduct(UUID id, Merchant merchant, UpdateProductDto updateProductDto);

    void deleteProduct(UUID id, Merchant merchant);
}
