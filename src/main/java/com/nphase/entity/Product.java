package com.nphase.entity;

import java.math.BigDecimal;

public class Product {
    private final String name;
    private final BigDecimal pricePerUnit;
    private final int quantity;
    private final ProductCategory productCategory;

    public Product(String name, BigDecimal pricePerUnit, int quantity, ProductCategory productCategory) {
        this.name = name;
        this.pricePerUnit = pricePerUnit;
        this.quantity = quantity;
        this.productCategory = productCategory;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPricePerUnit() {
        return pricePerUnit;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }
}
