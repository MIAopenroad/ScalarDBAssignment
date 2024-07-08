package com.ec.product;

import com.scalar.db.exception.transaction.AbortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService service;
    @Autowired
    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public List<Product> getAllProducts() throws AbortException {
        return service.getAllProducts();
    }
    @PutMapping("/add")
    public boolean addProduct(@RequestBody Product product) throws AbortException {
        return service.addProduct(product);
    }
}
