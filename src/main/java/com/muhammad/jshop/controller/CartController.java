package com.muhammad.jshop.controller;

import com.muhammad.jshop.dto.CartDto;
import com.muhammad.jshop.exception.ResourceNotFoundException;
import com.muhammad.jshop.request.CartRequest;
import com.muhammad.jshop.response.ApiResponse;
import com.muhammad.jshop.service.cart.ICartService;
import lombok.RequiredArgsConstructor;
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
            return  ResponseEntity.ok(new ApiResponse("Error", e.getMessage()));
        }
    }

    @PostMapping("/addToCart")
    public ResponseEntity<ApiResponse> addToCart(@RequestBody CartRequest cartRequest) {
        try {
            CartDto cartDto = cartService.addToCart(cartRequest);
            return  ResponseEntity.ok(new ApiResponse("Success", cartDto));
        } catch (ResourceNotFoundException e) {
            return   ResponseEntity.ok(new ApiResponse("Error", e.getMessage()));
        }
    }
}
