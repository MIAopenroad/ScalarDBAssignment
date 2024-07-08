package com.ec.product;

import com.scalar.db.exception.transaction.AbortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository repository;

    @Autowired
    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> getAllProducts() throws AbortException {
        return repository.getAllProducts();
    }
    public boolean addProduct(Product product) throws AbortException {
        return repository.addProduct(product);
    }
}
