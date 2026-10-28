package com.muhammad.jshop.service.cart;

import com.muhammad.jshop.dto.CartDto;
import com.muhammad.jshop.exception.AlreadyExistException;
import com.muhammad.jshop.exception.ResourceNotFoundException;
import com.muhammad.jshop.model.Cart;
import com.muhammad.jshop.model.CartItem;
import com.muhammad.jshop.model.Product;
import com.muhammad.jshop.model.User;
import com.muhammad.jshop.repository.CartItemRepository;
import com.muhammad.jshop.repository.CartRepository;
import com.muhammad.jshop.repository.ProductRepository;
import com.muhammad.jshop.repository.UserRepository;
import com.muhammad.jshop.request.CartRequest;
import com.muhammad.jshop.request.ChangeQuantityRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CartService implements  ICartService {
    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;

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

        if (cartItemRepository.existsByCartIdAndProductId(cart.getId(), productId)) {
            throw new AlreadyExistException("Product is already in the cart");
        }

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(cartRequest.getQuantity());
        cartItem.setUnitPrice(new BigDecimal(product.getPrice()));
        cartItem.setTotalPrice();

        cart.addItems(cartItem);
        return this.toDto(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public CartDto changeQuantity(Long itemId, ChangeQuantityRequest request) {
        Long userId = request.getUserId();
        int delta = request.getQuantity();

        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + itemId));

        Cart cart = item.getCart();
        if (!cart.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Cart item not found with id: " + itemId);
        }

        int newQuantity = item.getQuantity() + delta;

        if (newQuantity <= 0) {
            cart.removeItems(item);
        } else {
            item.setQuantity(newQuantity);
            item.setTotalPrice();
        }

        cart.updateTotalAmount();
        this.cartItemRepository.saveAndFlush(item);
        this.cartRepository.save(cart);
        return toDto(cart);
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        Cart cart = getOrCreateCart(userId);
        cart.clear();
    }


    // helpers
    private Cart getOrCreateCart(Long userId) {
        return cartRepository.findCartByUserId(userId).orElseGet(() -> {
            User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found"));
            Cart newCart = new Cart();
            newCart.setCartItems(null);
            newCart.setTotalAmount(BigDecimal.ZERO);
            newCart.setUser(user);
            newCart.setCartItems(new HashSet<>());
            cartRepository.save(newCart);
            return  cartRepository.save(newCart);
        });
    }

    private CartDto toDto(Cart cart) {
        return  modelMapper.map(cart, CartDto.class);
    }
}
