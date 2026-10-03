package com.muhammad.jshop.service.category;

import com.muhammad.jshop.dto.CategoryDto;
import com.muhammad.jshop.dto.ImageDto;
import com.muhammad.jshop.exception.AlreadyExistException;
import com.muhammad.jshop.mapper.CategoryMapper;
import com.muhammad.jshop.model.Category;
import com.muhammad.jshop.model.Image;
import com.muhammad.jshop.repository.CategoryRepository;
import com.muhammad.jshop.request.CategoryRequest;
import com.muhammad.jshop.response.ApiResponse;
import com.muhammad.jshop.service.image.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryInterface {
    private final CategoryRepository categoryRepository;
    private final IImageService imageService;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryDto addCategory(CategoryRequest categoryRequest) {
        try {
            if (categoryRepository.existsByName(String.valueOf(categoryRequest.getName()))) {
                throw new AlreadyExistException("Category already exist");
            }
            Image image = imageService.getImageById(categoryRequest.getImageId());

            Category category = new Category();
            category.setName(categoryRequest.getName());
            category.setImage(image);
            category.setDescription(categoryRequest.getDescription());
            Category savedCategory = categoryRepository.save(category);

            return categoryMapper.toDto(savedCategory);
        } catch (Exception e) {
           throw new AlreadyExistException("Category already exist " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories() {
        try {
            return categoryRepository.findAll().stream()
                    .map(categoryMapper::toDto).toList();
        }catch (RuntimeException e) {
            throw new RuntimeException("Failed to get all categories " + e.getMessage());
        }
    }
}
