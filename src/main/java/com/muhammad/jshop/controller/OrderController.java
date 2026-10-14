package com.muhammad.jshop.controller;

import com.muhammad.jshop.dto.OrderDto;
import com.muhammad.jshop.enums.OrderStatus;
import com.muhammad.jshop.request.PlaceOrderRequest;
import com.muhammad.jshop.request.UpdateOrderStatusRequest;
import com.muhammad.jshop.response.ApiResponse;
import com.muhammad.jshop.response.PageResponse;
import com.muhammad.jshop.service.order.IOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    private final IOrderService orderService;


    @PostMapping("/place")
    public ResponseEntity<ApiResponse> placeOrder(@Valid @RequestBody PlaceOrderRequest request) {
        OrderDto order = orderService.placeOrder(request.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Order placed successfully", order));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse> getOrder(
            @PathVariable Long orderId,
            @RequestParam Long userId) {
        return ResponseEntity.ok(new ApiResponse("Success", orderService.getOrder(orderId, userId)));
    }

    @GetMapping("/my-orders")
    public ResponseEntity<ApiResponse> getMyOrders(
            @RequestParam Long userId,
            @ParameterObject @PageableDefault(size = 10, sort = "orderDate", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse("Success",
                PageResponse.of(orderService.getUserOrders(userId, pageable))));
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponse> cancelOrder(
            @PathVariable Long orderId,
            @RequestParam Long userId) {
        return ResponseEntity.ok(new ApiResponse("Order cancelled", orderService.cancelOrder(orderId, userId)));
    }


    @GetMapping("/admin/all")
    public ResponseEntity<ApiResponse> getAllOrders(
            @RequestParam(required = false) OrderStatus status,
            @ParameterObject @PageableDefault(size = 10, sort = "orderDate", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse("Success",
                PageResponse.of(orderService.getAllOrders(status, pageable))));
    }

    @PatchMapping("/admin/{orderId}/status")
    public ResponseEntity<ApiResponse> updateStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateOrderStatusRequest request) {
        return ResponseEntity.ok(new ApiResponse("Order status updated",
                orderService.updateStatus(orderId, request.getStatus())));
    }
}
