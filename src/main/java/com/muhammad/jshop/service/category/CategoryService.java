package com.muhammad.jshop.service.category;

import com.muhammad.jshop.dto.CategoryDto;
import com.muhammad.jshop.exception.AlreadyExistException;
import com.muhammad.jshop.exception.ResourceNotFoundException;
import com.muhammad.jshop.mapper.CategoryMapper;
import com.muhammad.jshop.model.Category;
import com.muhammad.jshop.model.Image;
import com.muhammad.jshop.repository.CategoryRepository;
import com.muhammad.jshop.request.CategoryRequest;
import com.muhammad.jshop.service.image.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    @Transactional(readOnly = true)
    @Override
    public Page<CategoryDto> getAllCategories(String search, Pageable pageable) {
        try {
            Page<Category> page = (search == null || search.isBlank())
                    ? categoryRepository.findAll(pageable)
                    : categoryRepository.findByNameContainingIgnoreCase(search.trim(), pageable);
            return page
                    .map(categoryMapper::toDto);
        }catch (RuntimeException e) {
            throw new RuntimeException("Failed to get all categories " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto findCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return categoryMapper.toDto(category);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long categoryId, CategoryRequest categoryRequest) {
       Category oldCategory = categoryRepository.findById(categoryId)
               .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

       if (categoryRequest.getName() != null && !categoryRequest.getName().isBlank()) {
           if (categoryRepository.existsByNameAndIdNot(categoryRequest.getName(), categoryId)) {
               throw new AlreadyExistException("Category already exists: " + categoryRequest.getName());
           }
           oldCategory.setName(categoryRequest.getName());
       }

       if (categoryRequest.getDescription() != null && !categoryRequest.getDescription().isBlank()) {
           oldCategory.setDescription(categoryRequest.getDescription());
       }

       if (categoryRequest.getImageId() != null) {
           oldCategory.setImage(imageService.getImageById(categoryRequest.getImageId()));
       }

       return  categoryMapper.toDto(categoryRepository.save(oldCategory));
    }

    @Override
    @Transactional
    public void deleteCategory(Long categoryId) {
        categoryRepository.findById(categoryId)
                .ifPresentOrElse(categoryRepository::delete, () -> {
                    throw new ResourceNotFoundException("Category not found!");
                });
    }
}
