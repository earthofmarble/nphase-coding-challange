package com.nphase.service;


import com.nphase.entity.Product;
import com.nphase.entity.ProductCategory;
import com.nphase.entity.ShoppingCart;
import java.math.BigDecimal;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ShoppingCartServiceTest {

  private final ShoppingCartService service = new ShoppingCartService();

  @Test
  public void calculateTotalPriceTest() {
    ShoppingCart cart = new ShoppingCart(Arrays.asList(
        new Product("Tea", BigDecimal.valueOf(5.0), 2, ProductCategory.DRINKS),
        new Product("Coffee", BigDecimal.valueOf(6.5), 1, ProductCategory.DRINKS)
    ));

    BigDecimal result = service.calculateTotalPrice(cart);

    Assertions.assertEquals(result, BigDecimal.valueOf(16.5));
  }

  @Test
  public void calculatePriceWithBulkDiscount() {
    ShoppingCart cart = new ShoppingCart(Arrays.asList(
        new Product("Tea", BigDecimal.valueOf(5.1), 3, ProductCategory.DRINKS),
        new Product("Coffee", BigDecimal.valueOf(6.5), 1, ProductCategory.DRINKS)
    ));

    BigDecimal result = service.calculatePriceWithBulkDiscount(cart);

    Assertions.assertEquals(result, BigDecimal.valueOf(20.27));
  }

  @Test
  public void calculatePriceWithBulkNCategoryDiscountsAllDiscountedTest() {
    ShoppingCart cart = new ShoppingCart(Arrays.asList(
        new Product("Tea", BigDecimal.valueOf(150), 2, ProductCategory.DRINKS),
        new Product("Coffee", BigDecimal.valueOf(85), 4, ProductCategory.DRINKS),
        new Product("Apple", BigDecimal.valueOf(60), 5, ProductCategory.FOOD),
        new Product("Blueberry", BigDecimal.valueOf(150), 1, ProductCategory.FOOD),
        new Product("Raspberry", BigDecimal.valueOf(70), 2, ProductCategory.FOOD)
    ));

    BigDecimal result = service.calculatePriceWithBulkNCategoryDiscounts(cart);
    Assertions.assertEquals(result, BigDecimal.valueOf(1107.0));

  }

  @Test
  public void calculatePriceWithBulkNCategoryDiscountsFoodDiscountedTest() {
    ShoppingCart cart = new ShoppingCart(Arrays.asList(
        new Product("Tea", BigDecimal.valueOf(150), 2, ProductCategory.DRINKS),
        new Product("Apple", BigDecimal.valueOf(60), 5, ProductCategory.FOOD),
        new Product("Blueberry", BigDecimal.valueOf(150), 1, ProductCategory.FOOD),
        new Product("Raspberry", BigDecimal.valueOf(70), 2, ProductCategory.FOOD)
    ));

    BigDecimal result = service.calculatePriceWithBulkNCategoryDiscounts(cart);
    Assertions.assertEquals(result, BigDecimal.valueOf(831.0));

  }

}