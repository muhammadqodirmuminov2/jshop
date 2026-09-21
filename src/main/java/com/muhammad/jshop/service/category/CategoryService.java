package com.muhammad.jshop.service.category;

import com.muhammad.jshop.model.Category;
import com.muhammad.jshop.repository.CategoryRepository;
import com.muhammad.jshop.request.CategoryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryInterface {
    private final CategoryRepository categoryRepository;

    @Override
    public Category addCategory(CategoryRequest categoryRequest) {
        return null;
    }
}
