package com.muhammad.jshop.controller;

import com.muhammad.jshop.dto.ProductDto;
import com.muhammad.jshop.exception.ResourceNotFoundException;
import com.muhammad.jshop.request.ProductFilter;
import com.muhammad.jshop.request.ProductRequest;
import com.muhammad.jshop.request.ProductUpdateRequest;
import com.muhammad.jshop.response.ApiResponse;
import com.muhammad.jshop.response.PageResponse;
import com.muhammad.jshop.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private final IProductService productService;

    @PostMapping("add")
    public ResponseEntity<ApiResponse> createProduct(@RequestBody ProductRequest productRequest) {
        try {
            ProductDto productDto = productService.createProduct(productRequest);
            return ResponseEntity.ok(new ApiResponse("Product created successfully", productDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("all")
    public  ResponseEntity<ApiResponse> getAllProducts(
            @ParameterObject ProductFilter productFilter,
            @ParameterObject @PageableDefault(size = 10, sort = "name") Pageable pageable
    ) {
        try {
            Page<ProductDto> products = productService.getAllProducts(productFilter, pageable);
            return  ResponseEntity.ok(new ApiResponse("Success", PageResponse.of(products)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("find-one/{productId}")
    public ResponseEntity<ApiResponse> findOneProduct(@PathVariable("productId") long productId) {
        try {
            ProductDto productDto = productService.getOneProduct(productId);
            return  ResponseEntity.ok(new ApiResponse("Success", productDto));
        } catch (ResourceNotFoundException e) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PatchMapping("update/{productId}")
    public ResponseEntity<ApiResponse> updateProduct(
            @PathVariable("productId") Long productId,
            @RequestBody ProductUpdateRequest productRequest
    ) {
        try {
            ProductDto productDto = productService.updateProduct(productId, productRequest);
            return  ResponseEntity.ok(new ApiResponse("Success", productDto));
        } catch (Exception e) {
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok(new ApiResponse("Product deleted successfully", null));
    }
}
