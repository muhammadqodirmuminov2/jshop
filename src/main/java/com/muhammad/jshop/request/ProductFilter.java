package com.muhammad.jshop.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductFilter {
    private String search;
    private Long categoryId;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}
