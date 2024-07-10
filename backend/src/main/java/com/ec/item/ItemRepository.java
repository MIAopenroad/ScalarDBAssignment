package com.ec.item;

import com.scalar.db.api.*;
import com.scalar.db.exception.transaction.AbortException;
import com.scalar.db.io.Key;
import com.scalar.db.service.TransactionFactory;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class ItemRepository {
    private final DistributedTransactionManager manager;
    public ItemRepository() throws IOException {
        TransactionFactory factory = TransactionFactory.create("database.properties");
        this.manager = factory.getTransactionManager();
    }
    public List<Item> getAllItems() throws AbortException {
        DistributedTransaction transaction = null;
        try {
            transaction = this.manager.start();
            List<Result> res = transaction.scan(
                    Scan.newBuilder()
                    .namespace("item")
                    .table("item_info")
                            .all()
                            .where(ConditionBuilder.column("stock").isGreaterThanInt(0))
                            .projections("item_id", "name", "price", "stock", "description", "image_url")
                            .build()
            );
            List<Item> items = new ArrayList<>();
            for(Result item : res) {
                String itemId = item.getText("item_id");
                String name = item.getText("name");
                Double price = item.getDouble("price");
                int stock = item.getInt("stock");
                String description = item.getText("description");
                String imageUrl = item.getText("image_url");
                items.add(new Item(itemId, name,  price, stock, description, imageUrl));
            }
            transaction.commit();
            return items;
        } catch(Exception e) {
            if(transaction != null) {
                transaction.abort();
            }
            return new ArrayList<>();
        }
    }

    public boolean upsertItem(Item item) throws AbortException {
        DistributedTransaction transaction = null;
        String itemId = item.getItemId();
        String name = item.getName();
        Double price = item.getPrice();
        int stock = item.getStock();
        String description = item.getDescription();
        String imageUrl = item.getImageUrl();
        try {
            transaction = this.manager.start();
            if (itemId == null) {
                itemId = UUID.randomUUID().toString();
            }
            transaction.upsert(
                    Upsert.newBuilder()
                            .namespace("item")
                            .table("item_info")
                            .partitionKey(Key.ofText("item_id", itemId))
                            .textValue("name", name)
                            .doubleValue("price", price)
                            .intValue("stock", stock)
                            .textValue("description", description)
                            .textValue("image_url", imageUrl)
                            .build()
            );
            transaction.commit();
            return true;
        } catch (Exception e) {
            if(transaction != null) {
                transaction.abort();
            }
            return false;
        }
    }
}
