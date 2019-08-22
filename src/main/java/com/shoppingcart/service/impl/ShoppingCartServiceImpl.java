package com.shoppingcart.service.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import com.shoppingcart.exception.NotEnoughProductsInStockException;
import com.shoppingcart.logger.ILoggerUtil;
import com.shoppingcart.model.Product;
import com.shoppingcart.repository.ProductRepository;
import com.shoppingcart.service.ShoppingCartService;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ILoggerUtil loggerUtil;

    private Map<Product, Integer> products = new HashMap<>();

    /**
     * If product is in the map just increment quantity by 1. If product is not in
     * the map with, add it with quantity 1
     *
     * @param product
     */
    @Override
    public void add(Product product) {
        loggerUtil.log(Level.INFO, "ShoppingCartServiceImpl", "add", "start", product);
        if (products.containsKey(product)) {
            products.replace(product, products.get(product) + 1);
        } else {
            products.put(product, 1);
        }
        loggerUtil.log(Level.INFO, "ShoppingCartServiceImpl", "add", "success", product);
    }

    /**
     * If product is in the map with quantity > 1, just decrement quantity by 1. If
     * product is in the map with quantity 1, remove it from map
     *
     * @param product
     */
    @Override
    public void remove(Product product) {
        loggerUtil.log(Level.INFO, "ShoppingCartServiceImpl", "remove", "start", product);
        if (products.containsKey(product)) {
            if (products.get(product) > 1)
                products.replace(product, products.get(product) - 1);
            else if (products.get(product) == 1) {
                products.remove(product);
            }
        }
        loggerUtil.log(Level.INFO, "ShoppingCartServiceImpl", "remove", "success", product);
    }

    /**
     * @return unmodifiable copy of the map
     */
    @Override
    public Map<Product, Integer> getProductsInCart() {
        return Collections.unmodifiableMap(products);
    }

    @Override
    public BigDecimal checkout() throws NotEnoughProductsInStockException {
        loggerUtil.log(Level.INFO, "ShoppingCartServiceImpl", "checkout", "start");
        Product product;
        BigDecimal totalAmount = new BigDecimal(0.0d);
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            product = productRepository.findOne(entry.getKey().getId());
            if (entry.getValue() > product.getQuantity()) {
                loggerUtil.log(Level.ERROR, "ShoppingCartServiceImpl", "checkout", "error",
                        "Not Enough Products in the Cart");
                throw new NotEnoughProductsInStockException(product);
            }
            int decrementQuantity = product.getQuantity() - entry.getValue();
            totalAmount = totalAmount.add(new BigDecimal(decrementQuantity).multiply(product.getPrice()));
            entry.getKey().setQuantity(decrementQuantity);
        }
        productRepository.save(products.keySet());
        productRepository.flush();
        products.clear();
        loggerUtil.log(Level.INFO, "ShoppingCartServiceImpl", "checkout", "success");
        return totalAmount;
    }

    @Override
    public BigDecimal getTotal() {
        return products.entrySet().stream()
                .map(entry -> entry.getKey().getPrice().multiply(BigDecimal.valueOf(entry.getValue())))
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }
}
