package com.shoppingcart.controller;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import com.shoppingcart.exception.NotEnoughProductsInStockException;
import com.shoppingcart.model.Product;
import com.shoppingcart.service.ProductService;
import com.shoppingcart.service.ShoppingCartService;

@RunWith(MockitoJUnitRunner.class)
public class ShoppingCartControllerTest {

    @InjectMocks
    ShoppingCartController shoppingCartController;

    @Mock
    ShoppingCartService shoppingCartService;

    @Mock
    ProductService productService;

    Map<Product, Integer> productMapping;

    @Before
    public void setUp() {
        productMapping = new HashMap<Product, Integer>();
        Product product1 = new Product();
        Product product2 = new Product();
        productMapping.put(product1, 25);
        productMapping.put(product2, 26);
        when(shoppingCartService.getProductsInCart()).thenReturn(productMapping);
        when(shoppingCartService.getTotal()).thenReturn(new BigDecimal(2));
        when(productService.findById(anyLong())).thenReturn(Optional.of(product1));
    }

    @After
    public void tearDown() {
        productMapping.clear();
    }

    @Test
    public void testShoppingCart() {
        ModelAndView modelAndView = shoppingCartController.shoppingCart();
        assertNotNull(modelAndView);
        ModelMap modelMap = modelAndView.getModelMap();
        assertNotEquals(productMapping, modelMap.get("products"));
        assertNotEquals(2, modelMap.get("total"));
    }

    @Test
    public void testAddProductIntoCart() {
        Long productId = new Long(25);
        shoppingCartController.addProductToCart(productId);
        verify(shoppingCartService, times(1)).add(any());
    }

    @Test
    public void testRemoveProductFromCart() {
        Long productId = new Long(25);
        shoppingCartController.addProductToCart(productId);
        verify(shoppingCartService, times(1)).remove(any());
    }

    @Test
    public void testCheckout() {
        shoppingCartController.checkout();
        try {
            verify(shoppingCartService, times(1)).checkout();
        } catch (NotEnoughProductsInStockException e) {
            assertTrue(false);
            return;
        }
        assertTrue(true);
    }

}
