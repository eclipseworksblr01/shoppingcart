package com.shoppingcart.service.impl;

import java.util.Optional;
import org.apache.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.shoppingcart.logger.ILoggerUtil;
import com.shoppingcart.model.Product;
import com.shoppingcart.repository.ProductRepository;
import com.shoppingcart.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ILoggerUtil loggerUtil;

    @Autowired
    ProductRepository productRepository;

    @Override
    public Page<Product> findAllProductsPageable(Pageable pageable) {
        if (pageable == null) {
            loggerUtil.log(Level.ERROR, "ProductServiceImpl", "findAllProductsPageable", "error", pageable);
            return null;
        }
        loggerUtil.log(Level.INFO, "ProductServiceImpl", "findAllProductsPageable", "start", pageable);
        Page<Product> findAll = productRepository.findAll(pageable);
        loggerUtil.log(Level.INFO, "ProductServiceImpl", "findAllProductsPageable", "success");
        return findAll;
    }

    @Override
    public Optional<Product> findById(Long id) {
        Optional<Product> findById = null;
        if (id == null) {
            loggerUtil.log(Level.ERROR, "ProductServiceImpl", "findById", "error", id);
            return findById;
        }
        loggerUtil.log(Level.INFO, "ProductServiceImpl", "findById", "start", id);
        findById = productRepository.findById(id);
        loggerUtil.log(Level.INFO, "ProductServiceImpl", "findById", "success", id);
        return findById;
    }
}
