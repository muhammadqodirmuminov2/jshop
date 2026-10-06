package com.muhammad.jshop.repository;

import com.muhammad.jshop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductRepository extends JpaRepository<Product, Long> , JpaSpecificationExecutor<Product> {
    boolean existsByCategoryId(Long categoryId);
}
