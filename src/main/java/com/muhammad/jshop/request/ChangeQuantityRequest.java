package com.muhammad.jshop.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ChangeQuantityRequest {
    @Positive
    @NotNull
    private Long userId;

    @Positive
    @NotNull
    private int quantity;
}
