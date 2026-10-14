package com.muhammad.jshop.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PlaceOrderRequest {
    @NotNull(message = "User id must not be null")
    private Long userId;
}
