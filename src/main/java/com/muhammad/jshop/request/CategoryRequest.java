package com.muhammad.jshop.request;

import com.muhammad.jshop.model.Image;
import com.muhammad.jshop.model.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CategoryRequest {
    @NotBlank(message = "Name is required!")
    @NotNull(message = "Name is cannot be null!")
    private String name;

    private String description;

    @Min(value = 0, message = "Image id must be greater than 0!")
    @NotBlank(message = "Image id is required!")
    private Long imageId;
}
