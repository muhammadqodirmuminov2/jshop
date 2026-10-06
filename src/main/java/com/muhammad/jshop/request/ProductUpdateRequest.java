package com.muhammad.jshop.request;

import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;

@Data
public class ProductUpdateRequest {
    private String name;

    private String description;

    @Positive(message = "Price must be greater than 0")
    private Integer price;

    private Long categoryId;

    private List<Long> images;
}
