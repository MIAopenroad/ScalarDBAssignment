package com.ec.item;

import org.springframework.jmx.export.naming.IdentityNamingStrategy;

public class Item {
    private String itemId;
    private String name;
    private Double price;
    public Item() {}
    public Item(String itemId, String name, Double price) {
        this.itemId = itemId;
        this.name = name;
        this.price = price;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
