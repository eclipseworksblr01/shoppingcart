package com.shoppingcart.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
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
import com.shoppingcart.model.Product;
import com.shoppingcart.repository.ProductRepository;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceImplTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductServiceImpl productService;

    private Page<Product> pagedProducts;

    private List<Product> products;

    private static final int PAGE_SIZE = 5;

    @Before
    public void setUp() {
        products = new ArrayList<Product>();
        Product product = new Product();
        product.setName("product");
        for (int i = 0; i < 20; i++) {
            products.add(product);
        }
        Optional<Integer> testPage = Optional.of(5);
        Integer testPageIntValue = (testPage.get() < 0) ? 0 : testPage.get();
        PageRequest pageable = new PageRequest(testPageIntValue, PAGE_SIZE);
        pagedProducts = new PageImpl(products, pageable, PAGE_SIZE);
        when(productRepository.findAll(any(org.springframework.data.domain.Pageable.class))).thenReturn(pagedProducts);
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(product));
    }

    @After
    public void tearDown() {
        if (products != null) {
            products.clear();
        }
    }

    @Test
    public void testFindAllProductsPageable() {
        Optional<Integer> testPage = Optional.of(5);
        Integer testPageIntValue = (testPage.get() < 0) ? 0 : testPage.get();
        PageRequest pageable = new PageRequest(testPageIntValue, PAGE_SIZE);
        Page<Product> m_pagedProducts = productService.findAllProductsPageable(pageable);
        assertNotNull(m_pagedProducts);
        assertEquals(m_pagedProducts, pagedProducts);
    }

    @Test
    public void testFindById() {
        Product product = new Product();
        product.setName("product");
        Optional<Product> optionalProduct = productService.findById(product.getId());
        assertNotNull(optionalProduct);
        assertEquals(optionalProduct.get().getName(), product.getName());
    }

}
