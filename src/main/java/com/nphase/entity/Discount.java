package com.nphase.entity;

public class Discount {
  private Integer quantityForDiscount;
  private Double discountValue;

  public Discount(Integer quantityForDiscount, Double discountValue) {
    this.quantityForDiscount = quantityForDiscount;
    this.discountValue = discountValue;
  }

  public Integer getQuantityForDiscount() {
    return quantityForDiscount;
  }

  public void setQuantityForDiscount(Integer quantityForDiscount) {
    this.quantityForDiscount = quantityForDiscount;
  }

  public Double getDiscountValue() {
    return discountValue;
  }

  public void setDiscountValue(Double discountValue) {
    this.discountValue = discountValue;
  }
}
