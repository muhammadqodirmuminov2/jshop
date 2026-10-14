package com.muhammad.jshop.repository;

import com.muhammad.jshop.enums.OrderStatus;
import com.muhammad.jshop.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByUserId(Long userId, Pageable pageable);

    Page<Order> findByOrderStatus(OrderStatus status, Pageable pageable);

    Optional<Order> findByIdAndUserId(Long orderId, Long userId);
}
