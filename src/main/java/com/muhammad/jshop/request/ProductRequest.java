package com.muhammad.jshop.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;

@Data
public class ProductRequest {
    @NotBlank(message = "Product name can not be null!")
    @NotNull(message = "Product name can not be blank")
    private String name;

    private String description;

    @Positive(message = "Min value of price must be 0!")
    @NotBlank(message = "Product price must not be null")
    private int price;

    @Positive
    @NotNull
    @NotBlank
    private Long categoryId;

    List<Integer> images;
}
