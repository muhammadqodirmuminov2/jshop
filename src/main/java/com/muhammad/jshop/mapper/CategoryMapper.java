package com.muhammad.jshop.mapper;

import com.muhammad.jshop.dto.CategoryDto;
import com.muhammad.jshop.dto.ImageDto;
import com.muhammad.jshop.model.Category;
import com.muhammad.jshop.model.Image;
import org.springframework.stereotype.Component;


@Component
public class CategoryMapper {
    public CategoryDto toDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setDescription(category.getDescription());

        Image image = category.getImage();
        if (image != null) {
            ImageDto imageDto = new ImageDto();
            imageDto.setId(image.getId());
            imageDto.setFileName(image.getFileName());
            imageDto.setDownloadUrl(image.getDownloadUrl());
            categoryDto.setImage(imageDto);
        }
        return categoryDto;
    }
}
