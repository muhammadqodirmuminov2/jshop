package com.muhammad.jshop.service.order;

import com.muhammad.jshop.dto.OrderDto;
import com.muhammad.jshop.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IOrderService {
    OrderDto placeOrder(Long userId);
    OrderDto getOrder(Long orderId, Long userId);
    Page<OrderDto> getUserOrders(Long userId, Pageable pageable);
    OrderDto cancelOrder(Long orderId, Long userId);
    Page<OrderDto> getAllOrders(OrderStatus status, Pageable pageable);
    OrderDto updateStatus(Long orderId, OrderStatus status);
}
