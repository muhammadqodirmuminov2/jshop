package com.muhammad.jshop.service.category;

import com.muhammad.jshop.dto.CategoryDto;
import com.muhammad.jshop.model.Category;
import com.muhammad.jshop.request.CategoryRequest;

import java.util.List;

public interface ICategoryInterface {
    CategoryDto addCategory(CategoryRequest categoryRequest);
    List<CategoryDto> getAllCategories();
}
