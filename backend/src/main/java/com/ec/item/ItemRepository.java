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
                            .projections("item_id", "name", "price", "stock")
                            .build()
            );
            List<Item> items = new ArrayList<>();
            for(Result item : res) {
                String itemId = item.getText("item_id");
                String name = item.getText("name");
                Double price = item.getDouble("price");
                int stock = item.getInt("stock");
                items.add(new Item(itemId, name,  price, stock));
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
        System.out.println("id: " + item.getItemId());
        System.out.println("name: " + name);
        System.out.println("price: " + price);
        System.out.println("stock: " + stock);
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
