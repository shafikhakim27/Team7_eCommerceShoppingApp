package com.ecommerce.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for Product entity.
 * Uses JUnit 5 and AssertJ which are compatible with Java 21.
 */
@SpringBootTest
class ProductTest {

    @Test
    void testProductCreation() {
        // Given
        Product product = new Product("Test Product", "Test Description", "Electronics", 99.99);
        
        // When
        product.setProductBrand("Test Brand");
        
        // Then
        assertThat(product.getProductName()).isEqualTo("Test Product");
        assertThat(product.getProductDescription()).isEqualTo("Test Description");
        assertThat(product.getCategory()).isEqualTo("Electronics");
        assertThat(product.getProductPrice()).isEqualTo(99.99);
        assertThat(product.getProductBrand()).isEqualTo("Test Brand");
    }

    @Test
    void testProductDefaultConstructor() {
        // Given & When
        Product product = new Product();
        
        // Then
        assertThat(product).isNotNull();
        assertThat(product.getProductID()).isNull();
    }
}