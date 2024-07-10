package com.ec.order;

import java.util.Map;

public class OrderWithStatements extends Order {
    private Map<String, Integer> statements;

    public OrderWithStatements(String orderId, String customerId, String timestamp, boolean status,
                               Map<String, Integer> statements) {
        super(orderId, customerId, timestamp, status);
        this.statements = statements;
    }

    public Map<String, Integer> getStatements() {
        return statements;
    }

    public void setStatements(Map<String, Integer> statements) {
        this.statements = statements;
    }
}
