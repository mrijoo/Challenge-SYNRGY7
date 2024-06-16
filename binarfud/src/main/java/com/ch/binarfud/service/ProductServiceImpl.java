package com.ch.binarfud.service;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ch.binarfud.dto.product.request.CreateProductDto;
import com.ch.binarfud.dto.product.request.UpdateProductDto;
import com.ch.binarfud.dto.product.response.AllProductResponseDto;
import com.ch.binarfud.exception.DataNotFoundException;
import com.ch.binarfud.model.Merchant;
import com.ch.binarfud.model.Product;
import com.ch.binarfud.model.User;
import com.ch.binarfud.repository.ProductRepository;
import com.ch.binarfud.repository.UserRepository;

@Service
public class ProductServiceImpl implements ProductService {
    private final ModelMapper modelMapper;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper,
            UserRepository userRepository) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
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
    public Product addProduct(UUID userId, CreateProductDto createProductDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User with id " + userId + " not found"));
        Merchant merchant = user.getMerchant();
        Product product = modelMapper.map(createProductDto, Product.class);
        product.setMerchant(merchant);
        product.setMerchantId(merchant.getId());
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(UUID id, UUID userId, UpdateProductDto updateProductDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User with id " + userId + " not found"));
        Merchant merchant = user.getMerchant();
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Product not found"));

        if (!existingProduct.getMerchant().getId().equals(merchant.getId())) {
            // throw new AccessDeniedException("You are not authorized to update this
            // product");
            throw new IllegalStateException("You are not authorized to update this product");
        }

        modelMapper.map(updateProductDto, existingProduct);
        existingProduct.setId(id);

        UUID merchantId = existingProduct.getMerchant().getId();

        existingProduct.setMerchantId(merchantId);

        return productRepository.save(existingProduct);
    }

    @Override
    public void deleteProduct(UUID id, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User with id " + userId + " not found"));
        Merchant merchant = user.getMerchant();
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Product not found"));

        if (!existingProduct.getMerchant().getId().equals(merchant.getId())) {
            // throw new AccessDeniedException("You are not authorized to delete this
            // product");
            throw new IllegalStateException("You are not authorized to delete this product");
        }

        productRepository.delete(existingProduct);
    }
}
