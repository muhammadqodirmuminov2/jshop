package com.muhammad.jshop.repository;

import com.muhammad.jshop.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    boolean existsByCartIdAndProductId(Long cartId, Long productId);
    boolean existsByCartIdAndId(Long cartId, Long id);
}
