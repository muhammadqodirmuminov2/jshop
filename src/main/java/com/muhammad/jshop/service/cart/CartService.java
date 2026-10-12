package com.muhammad.jshop.service.cart;

import com.muhammad.jshop.dto.CartDto;
import com.muhammad.jshop.exception.ResourceNotFoundException;
import com.muhammad.jshop.model.Cart;
import com.muhammad.jshop.model.CartItem;
import com.muhammad.jshop.model.Product;
import com.muhammad.jshop.model.User;
import com.muhammad.jshop.repository.CartRepository;
import com.muhammad.jshop.repository.ProductRepository;
import com.muhammad.jshop.repository.UserRepository;
import com.muhammad.jshop.request.CartRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class CartService implements  ICartService {
    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public CartDto getMyCart(Long userId) {
        Cart cart = this.getOrCreateCart(userId);
        return this.toDto(cart);
    }

    @Override
    @Transactional
    public CartDto addToCart(CartRequest cartRequest) {
        Long productId = cartRequest.getProductId();
        Long userId = cartRequest.getUserId();

        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product not found with this id: " + productId));

        Cart cart = this.getOrCreateCart(userId);

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(cartRequest.getQuantity());
        cartItem.setUnitPrice(new BigDecimal(product.getPrice()));
        cartItem.setTotalPrice();

        cart.addItems(cartItem);
        return this.toDto(cartRepository.save(cart));
    }

    // helpers
    private Cart getOrCreateCart(Long userId) {
        return cartRepository.findCartByUserId(userId).orElseGet(() -> {
            User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found"));
            Cart newCart = new Cart();
            newCart.setUser(user);
            return  newCart;
        });
    }

    private CartDto toDto(Cart cart) {
        return  modelMapper.map(cart, CartDto.class);
    }
}
