package com.muhammad.jshop.service.cart;

import com.muhammad.jshop.dto.CartDto;
import com.muhammad.jshop.request.CartRequest;
import com.muhammad.jshop.request.ChangeQuantityRequest;

public interface ICartService {
    CartDto getMyCart(Long userId);
    CartDto addToCart(CartRequest cartRequest);
    CartDto changeQuantity(Long itemId, ChangeQuantityRequest changeQuantityRequest);
    void clearCart(Long userId);
}
