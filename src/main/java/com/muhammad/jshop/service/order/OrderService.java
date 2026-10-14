package com.muhammad.jshop.service.order;

import com.muhammad.jshop.dto.OrderDto;
import com.muhammad.jshop.enums.OrderStatus;
import com.muhammad.jshop.exception.ResourceNotFoundException;
import com.muhammad.jshop.mapper.OrderMapper;
import com.muhammad.jshop.model.*;
import com.muhammad.jshop.repository.CartRepository;
import com.muhammad.jshop.repository.OrderRepository;
import com.muhammad.jshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderDto placeOrder(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Cart cart = cartRepository.findCartByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("Cart is empty"));

        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PENDING);

        BigDecimal total = BigDecimal.ZERO;
        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();
            int quantity = cartItem.getQuantity();

            if (quantity <= 0) {
                throw new IllegalStateException("Invalid quantity for product: " + product.getName());
            }

            // price is taken from the product NOW, never from the client
            BigDecimal price = BigDecimal.valueOf(product.getPrice());
            OrderItem orderItem = new OrderItem(product, quantity, price);
            order.addItem(orderItem);
            total = total.add(orderItem.getTotalPrice());
        }
        order.setTotalPrice(total);

        Order saved = orderRepository.save(order);

        // empty the cart (orphanRemoval deletes cart_items rows)
        cart.getCartItems().clear();
        cart.updateTotalAmount();

        return orderMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrder(Long orderId, Long userId) {
        return orderMapper.toDto(getUserOrderOrThrow(orderId, userId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDto> getUserOrders(Long userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        return orderRepository.findByUserId(userId, pageable).map(orderMapper::toDto);
    }

    @Override
    @Transactional
    public OrderDto cancelOrder(Long orderId, Long userId) {
        Order order = getUserOrderOrThrow(orderId, userId);
        changeStatus(order, OrderStatus.CANCELLED);
        return orderMapper.toDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDto> getAllOrders(OrderStatus status, Pageable pageable) {
        Page<Order> page = (status == null)
                ? orderRepository.findAll(pageable)
                : orderRepository.findByOrderStatus(status, pageable);
        return page.map(orderMapper::toDto);
    }

    @Override
    @Transactional
    public OrderDto updateStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        changeStatus(order, status);
        return orderMapper.toDto(order);
    }

    // ---- helpers ----

    private Order getUserOrderOrThrow(Long orderId, Long userId) {
        return orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
    }

    private void changeStatus(Order order, OrderStatus next) {
        OrderStatus current = order.getOrderStatus();
        if (!current.canMoveTo(next)) {
            throw new IllegalStateException("Order status cannot change from " + current + " to " + next);
        }
        order.setOrderStatus(next);
    }
}
