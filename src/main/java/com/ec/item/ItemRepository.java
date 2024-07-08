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
            List<Result> items = transaction.scan(
                    Scan.newBuilder()
                    .namespace("item")
                    .table("item-info")
                            .all()
                            .projections("id", "name", "price")
                            .build()
            );
            List<Item> products = new ArrayList<>();
            for(Result item : items) {
                String id = item.getText("id");
                String name = item.getText("name");
                Double price = item.getDouble("price");
                products.add(new Item(id, name, price));
            }
            transaction.commit();
            return products;
        } catch(Exception e) {
            if(transaction != null) {
                transaction.abort();
            }
            return new ArrayList<>();
        }
    }

    public boolean addItem(Item item) throws AbortException {
        DistributedTransaction transaction = null;
        String name = item.getName();
        Double price = item.getPrice();
        try {
            transaction = this.manager.start();
            transaction.put(
                    Put.newBuilder()
                    .namespace("item")
                    .table("item-info")
                    .partitionKey(Key.ofText("id", UUID.randomUUID().toString()))
                    .textValue("name", name)
                    .doubleValue("price", price).build()
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
