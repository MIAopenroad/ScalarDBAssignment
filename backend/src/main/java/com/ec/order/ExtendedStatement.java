package com.ec.order;

import com.ec.item.Item;

public class ExtendedStatement extends Statements {
    private Item item;
    public ExtendedStatement(String statementId, String orderId, String itemId, int count, Item item) {
        super(statementId, orderId, itemId, count);
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
