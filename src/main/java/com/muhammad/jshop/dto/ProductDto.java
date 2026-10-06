package com.muhammad.jshop.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductDto {
    private int id;
    private String name;
    private String description;
    private int price;
    CategoryDto category;
    List<ImageDto> images;
}
