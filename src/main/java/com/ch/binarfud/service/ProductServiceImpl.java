package com.ch.binarfud.service;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.ch.binarfud.dto.product.request.CreateProductDto;
import com.ch.binarfud.dto.product.request.UpdateProductDto;
import com.ch.binarfud.dto.product.response.AllProductResponseDto;
import com.ch.binarfud.exception.DataNotFoundException;
import com.ch.binarfud.model.Merchant;
import com.ch.binarfud.model.Product;
import com.ch.binarfud.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {
    private final ModelMapper modelMapper;

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Page<AllProductResponseDto> getAllProduct(int page, int size) {
        Page<Product> products = productRepository.findAll(PageRequest.of(page, size));
        return products.map(product -> modelMapper.map(product, AllProductResponseDto.class));
    }

    @Override
    public Product getProductById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Product with id " + id + " not found"));
    }

    @Override
    public Product addProduct(Merchant merchant, CreateProductDto createProductDto) {
        Product product = modelMapper.map(createProductDto, Product.class);
        product.setMerchant(merchant);
        product.setMerchantId(merchant.getId());
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(UUID id, Merchant merchant, UpdateProductDto updateProductDto) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Product not found"));

        if (!existingProduct.getMerchant().getId().equals(merchant.getId())) {
            throw new AccessDeniedException("You are not authorized to update this product");
        }

        modelMapper.map(updateProductDto, existingProduct);
        existingProduct.setId(id);

        UUID merchantId = existingProduct.getMerchant().getId();

        existingProduct.setMerchantId(merchantId);

        return productRepository.save(existingProduct);
    }

    @Override
    public void deleteProduct(UUID id, Merchant merchant) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Product not found"));

        if (!existingProduct.getMerchant().getId().equals(merchant.getId())) {
            throw new AccessDeniedException("You are not authorized to delete this product");
        }

        productRepository.delete(existingProduct);
    }
}
