package com.ec.order;

import java.util.List;

public class OrderWithStatements extends Order {
    private List<Statements> statements;

    public OrderWithStatements(String orderId, String customerId, String timestamp, boolean status, List<Statements> statements) {
        super(orderId, customerId, timestamp, status);
        this.statements = statements;
    }

    public List<Statements> getStatements() {
        return statements;
    }

    public void setStatements(List<Statements> statements) {
        this.statements = statements;
    }
}
