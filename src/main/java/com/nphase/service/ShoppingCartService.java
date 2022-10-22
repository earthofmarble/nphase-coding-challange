package com.nphase.service;

import com.nphase.entity.Discount;
import com.nphase.entity.Product;
import com.nphase.entity.ProductCategory;
import com.nphase.entity.ShoppingCart;
import com.nphase.util.PropertyResolver;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCartService {

  private static final Double BULK_DISCOUNT = 0.1;
  private static final Integer BULK_MIN_ITEMS = 3;
  private static final String BULK_MIN_ITEMS_PROPERTY = "discount.bulk.min.items";
  private static final String BULK_DISCOUNT_VALUE_PROPERTY = "discount.bulk.value";
  private static final String CATEGORY_MIN_ITEMS_PROPERTY = "discount.{}.min.items";
  private static final String CATEGORY_DISCOUNT_VALUE_PROPERTY = "discount.{}.value";


  public BigDecimal calculateTotalPrice(ShoppingCart shoppingCart) {
    return shoppingCart.getProducts().stream()
        .map(product -> product.getPricePerUnit()
            .multiply(BigDecimal.valueOf(product.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public BigDecimal calculatePriceWithBulkDiscount(ShoppingCart shoppingCart) {
    int currentBulkMinItems =
        Integer.parseInt(PropertyResolver.readProperty(BULK_MIN_ITEMS_PROPERTY, BULK_MIN_ITEMS));
    double currentBulkDiscount =
        Double.parseDouble(PropertyResolver.readProperty(BULK_DISCOUNT_VALUE_PROPERTY, BULK_DISCOUNT));

    return shoppingCart.getProducts().stream()
        .map(product -> {
          BigDecimal bulkPrice = product.getPricePerUnit()
              .multiply(BigDecimal.valueOf(product.getQuantity()));

          return product.getQuantity() >= currentBulkMinItems ?
              bulkPrice.multiply(BigDecimal.valueOf(1 - currentBulkDiscount)) : bulkPrice;
        })
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public BigDecimal calculatePriceWithBulkNCategoryDiscounts(ShoppingCart shoppingCart) {
    int currentBulkMinItems =
        Integer.parseInt(PropertyResolver.readProperty(BULK_MIN_ITEMS_PROPERTY, BULK_MIN_ITEMS));
    double currentBulkDiscount =
        Double.parseDouble(
            PropertyResolver.readProperty(BULK_DISCOUNT_VALUE_PROPERTY, BULK_DISCOUNT));

    Map<ProductCategory, List<Product>> categoryMap = new HashMap<>();

    BigDecimal calculatedPrice = shoppingCart.getProducts().stream()
        .map(product -> {
          categoryMap
              .computeIfAbsent(product.getProductCategory(), value -> new ArrayList<>())
              .add(product);

          BigDecimal bulkPrice =
              product.getPricePerUnit().multiply(BigDecimal.valueOf(product.getQuantity()));

          return product.getQuantity() >= currentBulkMinItems ?
              bulkPrice.multiply(BigDecimal.valueOf(1 - currentBulkDiscount)) : bulkPrice;
        })
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal categoryDiscount = calculateCategoryDiscount(categoryMap);

    return calculatedPrice.subtract(categoryDiscount);
  }

  private BigDecimal calculateCategoryDiscount(
      Map<ProductCategory, List<Product>> categoryProductMap) {
    BigDecimal totalDiscount = BigDecimal.ZERO;
    for (Map.Entry<ProductCategory, List<Product>> categoryEntry : categoryProductMap.entrySet()) {
      ProductCategory category = categoryEntry.getKey();
      List<Product> categorizedProducts = categoryEntry.getValue();
      Integer productsInCategory =
          categorizedProducts.stream().map(Product::getQuantity).reduce(0, Integer::sum);

      if (productsInCategory >= loadMinQuantityForCategory(category)) {
        totalDiscount = categorizedProducts.stream()
            .map(product -> getProductDiscount(product, category))
            .reduce(totalDiscount, BigDecimal::add);
      }

    }
    return totalDiscount;
  }

  private BigDecimal getProductDiscount(Product product, ProductCategory productCategory) {
    int currentBulkMinItems =
        Integer.parseInt(PropertyResolver.readProperty(BULK_MIN_ITEMS_PROPERTY, BULK_MIN_ITEMS));
    double currentBulkDiscount =
        Double.parseDouble(PropertyResolver.readProperty(BULK_DISCOUNT_VALUE_PROPERTY, BULK_DISCOUNT));
    double currentDiscountForCategory = loadDiscountForCategory(productCategory);

    if (product.getQuantity() >= currentBulkMinItems) {
      if (currentDiscountForCategory > currentBulkDiscount) {
        return product.getPricePerUnit().multiply(BigDecimal.valueOf(product.getQuantity()))
            .multiply(BigDecimal.valueOf(currentDiscountForCategory));
      }

    } else {
      return product.getPricePerUnit().multiply(BigDecimal.valueOf(product.getQuantity()))
          .multiply(BigDecimal.valueOf(currentDiscountForCategory));
    }

    return BigDecimal.ZERO;
  }

  private int loadMinQuantityForCategory(ProductCategory category) {
    String builtPropertyForCategory =
        PropertyResolver.buildCategoryProperty(CATEGORY_MIN_ITEMS_PROPERTY, category);
    return Integer.parseInt(PropertyResolver.readProperty(
        builtPropertyForCategory, category.getDiscount().getQuantityForDiscount()));
  }

  private double loadDiscountForCategory(ProductCategory category) {
    String builtPropertyForCategory =
        PropertyResolver.buildCategoryProperty(CATEGORY_DISCOUNT_VALUE_PROPERTY, category);
    return Double.parseDouble(PropertyResolver.readProperty(
        builtPropertyForCategory, category.getDiscount().getDiscountValue()));
  }

}
