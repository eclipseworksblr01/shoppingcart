package com.shoppingcart.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.shoppingcart.model.Product;

import java.util.Optional;

public interface ProductService {

    public Optional<Product> findById(Long id);

    public Page<Product> findAllProductsPageable(Pageable pageable);

}
