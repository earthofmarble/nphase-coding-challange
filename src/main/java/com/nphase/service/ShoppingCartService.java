package com.nphase.service;

import com.nphase.entity.ShoppingCart;
import java.math.BigDecimal;

public class ShoppingCartService {
  public BigDecimal calculateTotalPrice(ShoppingCart shoppingCart) {
    return shoppingCart.getProducts().stream()
        .map(product -> product.getPricePerUnit()
            .multiply(BigDecimal.valueOf(product.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
