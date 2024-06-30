package com.ch.binarfud.controller;

import org.springframework.web.bind.annotation.RestController;

import com.ch.binarfud.dto.product.request.CreateProductDto;
import com.ch.binarfud.dto.product.request.UpdateProductDto;
import com.ch.binarfud.dto.product.response.AllProductResponseDto;
import com.ch.binarfud.dto.product.response.ProductResponseDto;
import com.ch.binarfud.model.Product;
import com.ch.binarfud.service.ProductService;
import com.ch.binarfud.util.ApiResponse;

import jakarta.validation.Valid;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ModelMapper modelMapper;
    private final ProductService productService;

    public ProductController(ModelMapper modelMapper, ProductService productService) {
        this.modelMapper = modelMapper;
        this.productService = productService;
    }

    @GetMapping("/_public")
    public ResponseEntity<Object> getAllProduct(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<AllProductResponseDto> productsPage = productService.getAllProduct(page, size);
        return ApiResponse.success(HttpStatus.OK, "Products has successfully retrieved", productsPage.getContent());
    }

    @GetMapping("/{id}/_public")
    public ResponseEntity<Object> getProductById(@PathVariable UUID id) {
        Product product = productService.getProductById(id);
        ProductResponseDto productResponseDto = modelMapper.map(product, ProductResponseDto.class);
        return ApiResponse.success(HttpStatus.OK, "Product has successfully retrieved", productResponseDto);
    }

    @PostMapping("/_merchant")
    public ResponseEntity<Object> addProduct(@RequestHeader("user_id") String userId,
            @Valid @RequestBody CreateProductDto createProductDto) {
        Product product = productService.addProduct(UUID.fromString(userId), createProductDto);
        ProductResponseDto productResponseDto = modelMapper.map(product, ProductResponseDto.class);
        return ApiResponse.success(HttpStatus.CREATED, "Product has successfully added", productResponseDto);
    }

    @PutMapping("/{id}/_merchant")
    public ResponseEntity<Object> updateProduct(@RequestHeader("user_id") String userId, @PathVariable UUID id,
            @Valid @RequestBody UpdateProductDto updateProductDto) {
        Product product = productService.updateProduct(id, UUID.fromString(userId), updateProductDto);
        ProductResponseDto productResponseDto = modelMapper.map(product, ProductResponseDto.class);
        return ApiResponse.success(HttpStatus.OK, "Product has successfully updated", productResponseDto);
    }

    @DeleteMapping("/{id}/_merchant")
    public ResponseEntity<Object> deleteProduct(@RequestHeader("user_id") String userId, @PathVariable UUID id) {
        productService.deleteProduct(id, UUID.fromString(userId));
        return ApiResponse.success(HttpStatus.OK, "Product has successfully deleted");
    }
}
