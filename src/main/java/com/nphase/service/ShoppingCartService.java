package com.nphase.service;

import com.nphase.entity.Product;
import com.nphase.entity.ProductCategory;
import com.nphase.entity.ShoppingCart;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
          return product.getQuantity() >= 3 ? bulkPrice.multiply(
              BigDecimal.valueOf(1 - BULK_DISCOUNT))
              : bulkPrice;
        })
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public BigDecimal calculatePriceWithBulkNCategoryDiscounts(ShoppingCart shoppingCart) {
    Map<ProductCategory, List<Product>> categoryMap = new HashMap<>();
    BigDecimal calculatedPrice = shoppingCart.getProducts().stream()
        .map(product -> {
          categoryMap.computeIfAbsent(
              product.getProductCategory(), value -> new ArrayList<>()).add(product);
          BigDecimal bulkPrice = product.getPricePerUnit()
              .multiply(BigDecimal.valueOf(product.getQuantity()));
          return product.getQuantity() >= 3 ?
              bulkPrice.multiply(BigDecimal.valueOf(1 - BULK_DISCOUNT)) : bulkPrice;
        })
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal categoryDiscount = calculateCategoryDiscount(categoryMap);

    return calculatedPrice.subtract(categoryDiscount);
  }

  private BigDecimal calculateCategoryDiscount(
      Map<ProductCategory, List<Product>> categoryProductMap) {
    BigDecimal discount = BigDecimal.ZERO;
    for (Map.Entry<ProductCategory, List<Product>> categoryEntry : categoryProductMap.entrySet()) {
      ProductCategory category = categoryEntry.getKey();
      List<Product> categorizedProducts = categoryEntry.getValue();
      Integer productsInCategory =
          categorizedProducts.stream().map(Product::getQuantity).reduce(0, Integer::sum);
      if (productsInCategory >= category.getQuantityForDiscount()) {
        discount = categorizedProducts.stream()
            .map(product -> getProductDiscount(product, category))
            .reduce(discount, BigDecimal::add);
      }

    }
    return discount;
  }

  private BigDecimal getProductDiscount(Product product, ProductCategory productCategory) {
    if (product.getQuantity() >= 3) {
      if (productCategory.getDiscount() > BULK_DISCOUNT) {
        return product.getPricePerUnit().multiply(BigDecimal.valueOf(product.getQuantity()))
            .multiply(BigDecimal.valueOf(productCategory.getDiscount()));
      }

    } else {
      return product.getPricePerUnit().multiply(BigDecimal.valueOf(product.getQuantity()))
          .multiply(BigDecimal.valueOf(productCategory.getDiscount()));
    }

    return BigDecimal.ZERO;
  }


}
