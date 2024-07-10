package com.ec.order;

import java.util.List;

public class OrderWithExtendedStatements extends Order {
    private List<ExtendedStatement> extendedStatements;

    public OrderWithExtendedStatements(String orderId, String customerId, String timestamp, boolean status, List<ExtendedStatement> extendedStatements) {
        super(orderId, customerId, timestamp, status);
        this.extendedStatements = extendedStatements;
    }

    public List<ExtendedStatement> getExtendedStatements() {
        return extendedStatements;
    }

    public void setExtendedStatements(List<ExtendedStatement> extendedStatements) {
        this.extendedStatements = extendedStatements;
    }
}
