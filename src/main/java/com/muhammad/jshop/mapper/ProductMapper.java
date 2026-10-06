package com.muhammad.jshop.mapper;

import com.muhammad.jshop.dto.CategoryDto;
import com.muhammad.jshop.dto.ImageDto;
import com.muhammad.jshop.dto.ProductDto;
import com.muhammad.jshop.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProductMapper {

    private final CategoryMapper categoryMapper;

    public ProductDto toDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setPrice(product.getPrice());
        productDto.setDescription(product.getDescription());

        CategoryDto categoryDto =  categoryMapper.toDto(product.getCategory());
        productDto.setCategory(categoryDto);

        productDto.setImages(product.getImages().stream().map(i -> {
            ImageDto imageDto = new ImageDto();
            imageDto.setId(i.getId());
            imageDto.setFileName(i.getFileName());
            imageDto.setDownloadUrl(i.getDownloadUrl());
            return  imageDto;
        }).toList());

        return  productDto;
    }
}
