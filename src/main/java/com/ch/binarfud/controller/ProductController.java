package com.ch.binarfud.controller;

import org.springframework.web.bind.annotation.RestController;

import com.ch.binarfud.dto.product.request.CreateProductDto;
import com.ch.binarfud.dto.product.request.UpdateProductDto;
import com.ch.binarfud.dto.product.response.AllProductResponseDto;
import com.ch.binarfud.dto.product.response.ProductResponseDto;
import com.ch.binarfud.model.Merchant;
import com.ch.binarfud.model.Product;
import com.ch.binarfud.model.User;
import com.ch.binarfud.service.ProductService;
import com.ch.binarfud.service.UserService;
import com.ch.binarfud.util.ApiResponse;

import jakarta.validation.Valid;

import java.security.Principal;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final ProductService productService;

    public ProductController(ModelMapper modelMapper, UserService userService, ProductService productService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllProduct(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<AllProductResponseDto> productsPage = productService.getAllProduct(page, size);
        return ApiResponse.success(HttpStatus.OK, "Products has successfully retrieved", productsPage.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getProductById(@PathVariable UUID id) {
        Product product = productService.getProductById(id);
        ProductResponseDto productResponseDto = modelMapper.map(product, ProductResponseDto.class);
        return ApiResponse.success(HttpStatus.OK, "Product has successfully retrieved", productResponseDto);
    }

    @PostMapping
    @PreAuthorize("hasRole('MERCHANT')")
    public ResponseEntity<Object> addProduct(@Valid @RequestBody CreateProductDto createProductDto,
            Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        Merchant merchant = user.getMerchant();

        Product product = productService.addProduct(merchant, createProductDto);
        ProductResponseDto productResponseDto = modelMapper.map(product, ProductResponseDto.class);
        return ApiResponse.success(HttpStatus.CREATED, "Product has successfully added", productResponseDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MERCHANT')")
    public ResponseEntity<Object> updateProduct(@PathVariable UUID id,
            @Valid @RequestBody UpdateProductDto updateProductDto, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        Merchant merchant = user.getMerchant();

        Product product = productService.updateProduct(id, merchant, updateProductDto);
        ProductResponseDto productResponseDto = modelMapper.map(product, ProductResponseDto.class);
        return ApiResponse.success(HttpStatus.OK, "Product has successfully updated", productResponseDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MERCHANT')")
    public ResponseEntity<Object> deleteProduct(@PathVariable UUID id, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        Merchant merchant = user.getMerchant();

        productService.deleteProduct(id, merchant);
        return ApiResponse.success(HttpStatus.OK, "Product has successfully deleted");
    }
}
