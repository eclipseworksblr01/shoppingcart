package com.shoppingcart.controller;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.servlet.ModelAndView;
import com.shoppingcart.model.Pager;
import com.shoppingcart.model.Product;
import com.shoppingcart.service.ProductService;

@RunWith(MockitoJUnitRunner.class)
public class HomeControllerTest {

    private static final int PAGE_SIZE = 5;

    @Mock
    ProductService productService;

    @InjectMocks
    HomeController homeController;

    private List<Product> products;

    @Before
    public void setUp() {
        products = new ArrayList<Product>();
        for (int i = 0; i < 20; i++) {
            products.add(new Product());
        }
    }

    @After
    public void tearDown() {
        if (products != null) {
            products.clear();
        }
    }

    @Test
    public void testHomeFirstPage() throws Exception {
        Optional<Integer> testPage = Optional.of(1);
        testHome(testPage);
    }

    @Test
    public void testHomeWithNegativePage() throws Exception {
        Optional<Integer> testPage = Optional.of(-1);
        testHome(testPage);
    }

    @Test
    public void testHomeLastPage() throws Exception {
        Optional<Integer> testPage = Optional.of(products.size() / 5);
        testHome(testPage);
    }

    private void testHome(Optional<Integer> testPage) {
        Integer testPageIntValue = (testPage.get() < 0) ? 0 : testPage.get();
        PageRequest pageable = new PageRequest(testPageIntValue, PAGE_SIZE);
        Page<Product> pagedProducts = new PageImpl(products, pageable, PAGE_SIZE);
        when(productService.findAllProductsPageable(any(PageRequest.class))).thenReturn(pagedProducts);
        ModelAndView modelAndView = homeController.home(testPage);
        assertNotNull(modelAndView);
        Map<String, Object> map = modelAndView.getModel();
        Page<Product> products = (Page<Product>) map.get("products");
        assertNotNull(products);
        Pager pager = (Pager) map.get("pager");
        assertNotNull(pager);
    }

}
