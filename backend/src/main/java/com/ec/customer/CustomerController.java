package com.ec.customer;

import com.scalar.db.exception.transaction.AbortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService service;
    @Autowired
    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @PostMapping("/signin")
    public boolean signin(@RequestBody Map<String, Object> reqBody) throws AbortException {
        String email = reqBody.get("email").toString();
        String password = reqBody.get("password").toString();
        return this.service.signin(email, password);
    }

    @PostMapping("/signup")
    public boolean signup(@RequestBody Map<String, Object> reqBody) throws AbortException {
        String email = reqBody.get("email").toString();
        String password = reqBody.get("password").toString();
        return this.service.signup(email, password);
    }
    @GetMapping("/all")
    public List<Customer> getAllCustomers() throws AbortException {
        return this.service.getAllCustomers();
    }
}
