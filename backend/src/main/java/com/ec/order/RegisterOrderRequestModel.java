package com.ec.order;

import java.util.Map;

public class RegisterOrderRequestModel {
    private String email;
    private Map<String, Integer> statements;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, Integer> getStatements() { return statements; }

    public void setStatements(Map<String, Integer> statements) {
        this.statements = statements;
    }
}
