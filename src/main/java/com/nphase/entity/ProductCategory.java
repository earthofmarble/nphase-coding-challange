package com.nphase.entity;

public enum ProductCategory {

    FOOD(3, 0.1),
    DRINKS(3, 0.1);

    private Integer quantityForDiscount;

    private Double discount;

    ProductCategory(Integer quantityForDiscount, Double discount) {
        this.quantityForDiscount = quantityForDiscount;
        this.discount = discount;
    }

    public Integer getQuantityForDiscount() {
        return quantityForDiscount;
    }

    public Double getDiscount() {
        return discount;
    }
}
