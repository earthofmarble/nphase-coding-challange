package com.nphase.util;

import com.nphase.entity.ProductCategory;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyResolver {

  private static final String PROPERTIES_LOCATION = "discounts.properties";

  public static String readProperty(String propertyName, Object defaultValue) {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    try (InputStream input = loader.getResourceAsStream(PROPERTIES_LOCATION)) {
      Properties properties = new Properties();
      properties.load(input);
      return properties.getProperty(propertyName, String.valueOf(defaultValue));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  public static String buildCategoryProperty(String property, ProductCategory productCategory) {
    return property.replace("{}", productCategory.name().toLowerCase());
  }
}
