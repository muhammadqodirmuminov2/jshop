package com.muhammad.jshop.repository;

import com.muhammad.jshop.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
    Page<Category> findByNameContainingIgnoreCase(String name, Pageable pageable);
    boolean existsByNameAndIdNot(String name, Long id);
}
