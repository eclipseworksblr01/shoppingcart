package com.shoppingcart.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import com.shoppingcart.model.ChargeRequest;
import com.shoppingcart.model.Product;
import com.shoppingcart.service.PaymentsService;
import com.shoppingcart.service.ProductService;
import com.shoppingcart.service.ShoppingCartService;
import com.stripe.model.Charge;

@RunWith(MockitoJUnitRunner.class)
public class ShoppingCartControllerTest {

    @InjectMocks
    ShoppingCartController shoppingCartController;

    @Mock
    ShoppingCartService shoppingCartService;

    @Mock
    PaymentsService paymentsService;

    @Mock
    ProductService productService;

    Map<Product, Integer> productMapping;

    @Before
    public void setUp() {
        productMapping = new HashMap<Product, Integer>();
        Product product1 = new Product();
        product1.setId(new Random().nextLong());
        Product product2 = new Product();
        product2.setId(new Random().nextLong());
        Charge charge = new Charge();
        charge.setId("charge_id");
        charge.setStatus("charge_status");
        productMapping.put(product1, 25);
        productMapping.put(product2, 26);
        when(shoppingCartService.getProductsInCart()).thenReturn(productMapping);
        BigDecimal totalProducts = new BigDecimal(2);
        BigDecimal totalAmount = new BigDecimal(150.0d);
        when(shoppingCartService.getTotal()).thenReturn(totalProducts);
        when(productService.findById(anyLong())).thenReturn(Optional.of(product1));
        try {
            when(paymentsService.charge(any(ChargeRequest.class))).thenReturn(charge);
            when(shoppingCartService.checkout()).thenReturn(totalAmount);
        } catch (Exception e) {
        }
    }

    @After
    public void tearDown() {
        productMapping.clear();
        productMapping = null;
    }

    @Test
    public void testShoppingCart() {
        ModelAndView modelAndView = shoppingCartController.shoppingCart();
        assertNotNull(modelAndView);
        ModelMap modelMap = modelAndView.getModelMap();
        assertEquals(productMapping, modelMap.get("products"));
        assertNotNull((String) modelMap.get("total"));
        assertEquals(Integer.parseInt((String) modelMap.get("total")), 2);
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
        shoppingCartController.removeProductFromCart(productId);
        verify(shoppingCartService, times(1)).remove(any());
    }

    @Test
    public void testCheckout() {
        try {
            ModelAndView modelAndView = shoppingCartController.checkout();
            verify(shoppingCartService, times(1)).checkout();
            verify(paymentsService, times(1)).charge(any(ChargeRequest.class));
            assertNotNull(modelAndView);
            ModelMap modelMap = modelAndView.getModelMap();
            assertEquals(modelMap.get("id"), "charge_id");
            assertEquals(modelMap.get("status"), "charge_status");
        } catch (Exception e) {
            assertTrue(false);
            return;
        }
    }

}
