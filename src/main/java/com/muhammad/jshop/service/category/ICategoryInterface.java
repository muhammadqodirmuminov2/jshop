package com.muhammad.jshop.service.category;

import com.muhammad.jshop.dto.CategoryDto;
import com.muhammad.jshop.model.Category;
import com.muhammad.jshop.request.CategoryRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ICategoryInterface {
    CategoryDto addCategory(CategoryRequest categoryRequest);
    Page<CategoryDto> getAllCategories(String search, Pageable pageable);
    CategoryDto findCategoryById(Long categoryId);
    CategoryDto updateCategory(Long categoryId, CategoryRequest categoryRequest);
    void deleteCategory(Long categoryId);
}
