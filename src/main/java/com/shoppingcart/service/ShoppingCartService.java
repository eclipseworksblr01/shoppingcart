package com.shoppingcart.service;

import com.shoppingcart.exception.NotEnoughProductsInStockException;
import com.shoppingcart.model.Product;

import java.math.BigDecimal;
import java.util.Map;

public interface ShoppingCartService {

    public void add(Product product);

    public void remove(Product product);

    public Map<Product, Integer> getProductsInCart();

    public BigDecimal checkout() throws NotEnoughProductsInStockException;

    public BigDecimal getTotal();
}
