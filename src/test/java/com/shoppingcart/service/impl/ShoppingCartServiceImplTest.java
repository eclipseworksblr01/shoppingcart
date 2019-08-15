package com.shoppingcart.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Random;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import com.shoppingcart.exception.NotEnoughProductsInStockException;
import com.shoppingcart.model.Product;
import com.shoppingcart.repository.ProductRepository;

@RunWith(MockitoJUnitRunner.class)
public class ShoppingCartServiceImplTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ShoppingCartServiceImpl shoppingCartService;

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testAdd() {
        assertEquals(shoppingCartService.getProductsInCart().size(), 0);
        Product product = new Product();
        product.setId(new Random().nextLong());
        testInternalAdd(product);
        assertEquals(shoppingCartService.getProductsInCart().size(), 1);
    }

    @Test
    public void testRemove() {
        Product product = new Product();
        product.setId(new Random().nextLong());
        testInternalAdd(product);
        assertEquals(shoppingCartService.getProductsInCart().size(), 1);
        shoppingCartService.remove(product);
        assertEquals(shoppingCartService.getProductsInCart().size(), 0);
    }

    @Test
    public void testProductsInCart() {
        testAdd();
        Map<Product, Integer> productsInCart = shoppingCartService.getProductsInCart();
        assertNotNull(productsInCart);
        assertEquals(productsInCart.size(), 1);
    }

    @Test
    public void testCheckout() {
        try {
            Product product = new Product();
            product.setQuantity(10);
            long nextLong = new Random().nextLong();
            product.setId(nextLong);
            testInternalAdd(product);
            when(productRepository.findOne(nextLong)).thenReturn(product);
            shoppingCartService.checkout();
            Map<Product, Integer> productsInCart = shoppingCartService.getProductsInCart();
            assertNotNull(productsInCart);
            assertEquals(productsInCart.size(), 0);
        } catch (NotEnoughProductsInStockException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testCheckoutNotEnoughProducts() {
        try {
            Product product = new Product();
            product.setQuantity(0);
            long nextLong = new Random().nextLong();
            product.setId(nextLong);
            testInternalAdd(product);
            when(productRepository.findOne(nextLong)).thenReturn(product);
            shoppingCartService.checkout();
            Map<Product, Integer> productsInCart = shoppingCartService.getProductsInCart();
            assertNotNull(productsInCart);
            assertEquals(productsInCart.size(), 0);
        } catch (NotEnoughProductsInStockException e) {
            assertTrue(true);
            return;
        }
        assertTrue(false);
    }

    @Test
    public void testGetTotal() {
        BigDecimal bigDecimal = shoppingCartService.getTotal();
        assertNotNull(bigDecimal);
    }

    private Product testInternalAdd(Product product) {
        shoppingCartService.add(product);
        return product;
    }

}
