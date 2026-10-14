package com.muhammad.jshop.controller;

import com.muhammad.jshop.dto.CartDto;
import com.muhammad.jshop.exception.ResourceNotFoundException;
import com.muhammad.jshop.request.CartRequest;
import com.muhammad.jshop.request.ChangeQuantityRequest;
import com.muhammad.jshop.response.ApiResponse;
import com.muhammad.jshop.service.cart.ICartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/carts")
public class CartController {
    private final ICartService cartService;

    @GetMapping("/my-cart")
    public ResponseEntity<ApiResponse> getMyCart(@RequestParam Long userId) {
        try {
            CartDto cartDto = cartService.getMyCart(userId);
            return ResponseEntity.ok(new ApiResponse("Success", cartDto));
        } catch (Exception e) {
            return  ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/addToCart")
    public ResponseEntity<ApiResponse> addToCart(@RequestBody CartRequest cartRequest) {
        try {
            CartDto cartDto = cartService.addToCart(cartRequest);
            return  ResponseEntity.ok(new ApiResponse("Success", cartDto));
        } catch (ResourceNotFoundException e) {
            return   ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PatchMapping("/update-quantity/{itemId}")
    public  ResponseEntity<ApiResponse> updateQuantity(
            @PathVariable Long itemId,
            @Valid @RequestBody ChangeQuantityRequest changeQuantityRequest
    ) {
        try {
            CartDto cartDto = cartService.changeQuantity(itemId, changeQuantityRequest);
            return  ResponseEntity.ok(new ApiResponse("Success", cartDto));
        } catch (Exception e) {
            return   ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/clear/{userId}")
    public  ResponseEntity<ApiResponse> clearCart(@PathVariable Long userId) {
        try {
            cartService.clearCart(userId);
            return   ResponseEntity.ok(new ApiResponse("Success", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(),null ));
        }
    }
}
