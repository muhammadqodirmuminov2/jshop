package com.muhammad.jshop.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CartRequest {
    @NotBlank(message = "product id is required")
    private Long productId;
    private Long userId;

    @Positive(message = "quantity must be a number")
    @NotBlank(message = "quantity")
    private int quantity;
}
