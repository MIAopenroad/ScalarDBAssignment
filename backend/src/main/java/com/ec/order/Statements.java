package com.ec.order;

public class Statements {
    private String statementId;
    private String orderId;
    private String itemId;
    private int count;
    public Statements() {}
    public Statements(String statementId, String orderId, String itemId, int count) {
        this.statementId = statementId;
        this.orderId = orderId;
        this.itemId = itemId;
        this.count = count;
    }

    public String getStatementId() { return statementId; }

    public void setStatementId(String statementId) {
        this.statementId = statementId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
}
