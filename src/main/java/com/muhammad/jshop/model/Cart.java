package com.muhammad.jshop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "carts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal totalAmount = BigDecimal.ZERO;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<CartItem> cartItems = new HashSet<>();

    public void addItems(CartItem cartItem) {
        this.cartItems.add(cartItem);
        cartItem.setCart(this);
        this.updateTotalAmount();
    }

    public  void removeItems(CartItem cartItem) {
        this.cartItems.remove(cartItem);
        cartItem.setCart(null);
        this.updateTotalAmount();
    }

    public void updateTotalAmount() {
        this.totalAmount = this.cartItems.stream().map(i-> {
            BigDecimal unitPrice = i.getUnitPrice();
            if (unitPrice == null) {
                return  BigDecimal.ZERO;
            }
            return  unitPrice.multiply(BigDecimal.valueOf(i.getQuantity()));
        }).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void clear() {
        this.cartItems.clear();
        this.updateTotalAmount();
    }
}
