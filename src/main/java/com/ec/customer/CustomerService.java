package com.ec.customer;

import com.scalar.db.exception.transaction.AbortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository repository;
    @Autowired
    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public boolean signin(String email, String password) throws AbortException {
        return this.repository.signin(email, password);
    }

    public boolean signup(String email, String password) throws AbortException {
        return this.repository.signup(email, password);
    }

    public List<Customer> getAllCustomers() throws AbortException {
        return this.repository.getAllCustomers();
    }
}
