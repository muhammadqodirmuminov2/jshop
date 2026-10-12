package com.muhammad.jshop.service.cart;

import com.muhammad.jshop.dto.CartDto;
import com.muhammad.jshop.request.CartRequest;

public interface ICartService {
    CartDto getMyCart(Long userId);
    CartDto addToCart(CartRequest cartRequest);
}
