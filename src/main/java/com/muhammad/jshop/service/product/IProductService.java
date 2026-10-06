package com.muhammad.jshop.service.product;

import com.muhammad.jshop.dto.ProductDto;
import com.muhammad.jshop.request.ProductFilter;
import com.muhammad.jshop.request.ProductRequest;
import com.muhammad.jshop.request.ProductUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IProductService {
    ProductDto createProduct(ProductRequest productRequest);
    Page<ProductDto> getAllProducts(ProductFilter productFilter, Pageable pageable);
    ProductDto getOneProduct(long productId);
    ProductDto updateProduct(Long productId, ProductUpdateRequest productRequest);

    @Transactional
    void deleteProduct(Long productId);
}
