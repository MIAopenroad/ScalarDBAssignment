package com.ec.order;

import com.scalar.db.exception.transaction.AbortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrderService {
    private final OrderRepository repository;
    @Autowired
    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }
    public List<OrderWithStatements> getAllOrders() throws AbortException {
        return this.repository.fetchAllOrders();
    }

    public boolean registerOrder(String email, Map<String, Integer> statements) throws AbortException {
        return this.repository.registerOrder(email, statements);
    }
    public List<OrderWithStatements> getOrdersByEmail(String email) throws AbortException {
        return this.repository.getOrdersByEmail(email);
    }
}
