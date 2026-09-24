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
        //find image by id

            // if  not exist throw a no data found error
        // check if exist any category with this name
            // if exist return message as already have
        // if all good save and return a category response
        return null;
    }
}
