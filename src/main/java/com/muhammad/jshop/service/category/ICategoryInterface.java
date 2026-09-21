package com.muhammad.jshop.service.category;

import com.muhammad.jshop.model.Category;
import com.muhammad.jshop.request.CategoryRequest;

public interface ICategoryInterface {
    Category addCategory(CategoryRequest categoryRequest);
}
