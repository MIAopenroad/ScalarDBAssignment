package com.ec.customer;

public class Customer {
    private String customerId;
    private String password;
    private String email;

    public Customer() {}
    public Customer(String customerId, String email, String password) {
        this.customerId = customerId;
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCostomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
