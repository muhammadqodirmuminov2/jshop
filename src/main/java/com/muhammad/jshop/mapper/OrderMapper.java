package com.muhammad.jshop.mapper;

import com.muhammad.jshop.dto.OrderDto;
import com.muhammad.jshop.dto.OrderItemDto;
import com.muhammad.jshop.model.Order;
import com.muhammad.jshop.model.OrderItem;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class OrderMapper {

    public OrderDto toDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setUserId(order.getUser().getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getOrderStatus());
        dto.setTotalPrice(order.getTotalPrice());

        List<OrderItemDto> items = order.getOrderItems().stream()
                .sorted(Comparator.comparing(OrderItem::getId, Comparator.nullsLast(Long::compareTo)))
                .map(this::toItemDto)
                .toList();
        dto.setItems(items);
        dto.setTotalItems(items.stream().mapToInt(OrderItemDto::getQuantity).sum());
        return dto;
    }

    private OrderItemDto toItemDto(OrderItem item) {
        OrderItemDto dto = new OrderItemDto();
        dto.setId(item.getId());
        dto.setProductId((long) item.getProduct().getId());
        dto.setProductName(item.getProduct().getName());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        dto.setTotalPrice(item.getTotalPrice());
        return dto;
    }
}
