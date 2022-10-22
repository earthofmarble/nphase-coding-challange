package com.nphase.entity;

public enum ProductCategory {

  FOOD(new Discount(3, 0.1)),
  DRINKS(new Discount(3, 0.1));
  private Discount discount;

  ProductCategory(Discount discount) {
    this.discount = discount;
  }

  public Discount getDiscount() {
    return discount;
  }
}
