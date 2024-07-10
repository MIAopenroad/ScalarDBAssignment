package com.ec.item;

import org.springframework.jmx.export.naming.IdentityNamingStrategy;

public class Item {
    private String itemId;
    private String name;
    private Double price;
    private int stock;
    private String description;
    private String imageUrl;
    public Item() {}
    public Item(String itemId, String name, Double price, int stock, String description, String imageUrl) {
        this.itemId = itemId;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String getItemId() { return itemId; }

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

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() { return imageUrl; }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
