package com.ec.order;

public class Order {
    private String orderId;
    private String customerId;
    private String timestamp;
    private boolean status;
    public Order() {}
    public Order(String orderId, String customerId, String timestamp, boolean status) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.timestamp = timestamp;
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    public boolean getStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
}
