package com.ec.order;

import com.scalar.db.exception.transaction.AbortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService service;
    @Autowired
    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public List<OrderWithStatements> getAllOrders() throws AbortException {
        return service.getAllOrders();
    }

    @PostMapping("/order")
    public boolean registerOrder(@RequestBody RegisterOrderRequestModel reqBody) throws AbortException {
        String email = reqBody.getEmail();
        Map<String, Integer> statements = reqBody.getStatements();
        return this.service.registerOrder(email, statements);
    }
    @GetMapping("/record/{email}")
    public List<OrderWithExtendedStatements> getOrdersByEmail(@PathVariable String email) throws AbortException {
        return this.service.getOrdersByEmail(email);
    }
}
