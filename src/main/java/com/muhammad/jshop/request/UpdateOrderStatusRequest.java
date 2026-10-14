package com.muhammad.jshop.request;

import com.muhammad.jshop.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateOrderStatusRequest {
    @NotNull(message = "Status must not be null")
    private OrderStatus status;
}
