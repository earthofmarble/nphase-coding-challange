package com.nphase.service;

import com.nphase.entity.ShoppingCart;
import java.math.BigDecimal;

public class ShoppingCartService {

  private static final Double BULK_DISCOUNT = 0.1;


  public BigDecimal calculateTotalPrice(ShoppingCart shoppingCart) {
    return shoppingCart.getProducts().stream()
        .map(product -> product.getPricePerUnit()
            .multiply(BigDecimal.valueOf(product.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public BigDecimal calculatePriceWithBulkDiscount(ShoppingCart shoppingCart) {
    return shoppingCart.getProducts().stream()
        .map(product -> {
          BigDecimal bulkPrice = product.getPricePerUnit()
              .multiply(BigDecimal.valueOf(product.getQuantity()));
          return product.getQuantity() >= 3 ? bulkPrice.multiply(BigDecimal.valueOf(1-BULK_DISCOUNT))
              : bulkPrice;
        })
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

}
