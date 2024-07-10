package com.ec.order;


import com.scalar.db.api.*;
import com.scalar.db.exception.transaction.AbortException;
import com.scalar.db.io.Key;
import com.scalar.db.service.TransactionFactory;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;

@Repository
public class OrderRepository {
    private final DistributedTransactionManager manager;
    public OrderRepository() throws IOException {
        TransactionFactory factory = TransactionFactory.create("database.properties");
        this.manager = factory.getTransactionManager();
    }
    public List<OrderWithStatements> fetchAllOrders() throws AbortException {
        DistributedTransaction transaction = null;
        try {
            transaction = this.manager.start();
            List<Result> res = transaction.scan(
                    Scan.newBuilder()
                            .namespace("order")
                            .table("orders")
                            .all()
                            .projections("order_id", "customer_id", "timestamp", "status")
                            .build()
            );
            List<Order> orders = new ArrayList<>();
            for(Result row : res) {
                String orderId = row.getText("order_id");
                String customerId = row.getText("customer_id");
                String timestamp = row.getText("timestamp");
                boolean status = row.getBoolean("status");
                System.out.println("orderId: " + orderId);
                System.out.println("customerId: " + customerId);
                System.out.println("timestamp: " + timestamp);
                System.out.println("status: " + status);
                System.out.println();
                orders.add(new Order(orderId, customerId, timestamp, status));
            }

            //orderIdに紐づいているstatementsを全部とってくる
            List<OrderWithStatements> orderWithStatementsList = new ArrayList<>();
            for(Order order: orders) {
                List<Result> rows = transaction.scan(
                        Scan.newBuilder()
                                .namespace("order")
                                .table("statements")
                                .all()
                                .where(ConditionBuilder.column("order_id").isEqualToText(order.getOrderId()))
                                .projections("statement_id", "order_id", "customer_id", "item_name", "count")
                                .build()
                );
                Map<String, Integer> statements = new HashMap<>();
                for(Result row : rows) {
                    String item_name = row.getText("item_name");
                    int count = row.getInt("count");
                    System.out.println("item_name: " + item_name);
                    System.out.println("count: " + count);
                    System.out.println();
                    statements.put(item_name, count);
                }
                orderWithStatementsList.add(
                        new OrderWithStatements(order.getOrderId(), order.getCustomerId(),
                                order.getTimestamp(), order.getStatus(), statements)
                );
            }
            transaction.commit();
            return orderWithStatementsList;
        } catch(Exception e) {
            e.printStackTrace();
            if(transaction != null) {
                transaction.abort();
            }
            return new ArrayList<>();
        }
    }

    //ユーザごとの注文履歴
    //入力: customerId
    //処理: customerIdでList<OrderId>を取得する
    public List<OrderWithStatements> getOrdersByEmail(String email) throws AbortException {
        //1. emailからcustomerIdを取得
        DistributedTransaction transaction = null;
        try {
            transaction = this.manager.start();
            Optional<Result> res = transaction.get(
                    Get.newBuilder()
                            .namespace("customer")
                            .table("customers")
                            .partitionKey(Key.ofText("email", email))
                            .projections("id", "email", "password")
                            .build()
            );
            if(!res.isPresent()) {
                transaction.abort();
                return new ArrayList<>();
            }
            String customerId = res.get().getText("id");
            //2. where customer_id == customerId
            List<Result> ordersByCustomer = transaction.scan(
                    Scan.newBuilder()
                            .namespace("order")
                            .table("orders")
                            .all()
                            .where(ConditionBuilder.column("customer_id").isEqualToText(customerId))
                            .projections("order_id", "timestamp", "status")
                            .build()
            );
            List<OrderWithStatements> resp = new ArrayList<>();
            for(Result orderByCustomer : ordersByCustomer) {
                String orderId = orderByCustomer.getText("order_id");
                String timestamp = orderByCustomer.getText("timestamp");
                boolean status = orderByCustomer.getBoolean("status");

                List<Result> statementsByOrderId = transaction.scan(
                        Scan.newBuilder()
                                .namespace("order")
                                .table("statements")
                                .all()
                                .where(ConditionBuilder.column("order_id").isEqualToText(orderId))
                                .projections("statement_id", "order_id", "customer_id", "item_name", "count")
                                .build()
                );
                Map<String, Integer> statements = new HashMap<>();
                for(Result statement: statementsByOrderId) {
                    String itemName = statement.getText("item_name");
                    int count = statement.getInt("count");
                    statements.put(itemName, count);
                }
                resp.add(new OrderWithStatements(orderId, customerId, timestamp, status, statements));
            }
            transaction.commit();
            return resp;
        } catch(Exception e) {
            e.printStackTrace();
            if(transaction != null) {
                transaction.abort();
            }
            return new ArrayList<>();
        }
    }

    public boolean registerOrder(String email, Map<String, Integer> statements) throws AbortException {
        //1. emailからcustomerIdを取得
        DistributedTransaction transaction = null;
        try {
            transaction = this.manager.start();
            Optional<Result> res = transaction.get(
                    Get.newBuilder()
                            .namespace("customer")
                            .table("customers")
                            .partitionKey(Key.ofText("email", email))
                            .projections("id", "email", "password")
                            .build()
            );
            if(!res.isPresent()) {
                transaction.abort();
                return false;
            }
            String customerId = res.get().getText("id");
            String orderId = UUID.randomUUID().toString();
            transaction.insert(
                    Insert.newBuilder()
                            .namespace("order")
                            .table("orders")
                            .partitionKey(Key.ofText("order_id", orderId))
                            .textValue("customer_id", customerId)
                            .textValue("timestamp", new Date().toString())
                            .booleanValue("status", true)
                            .build()
            );
            for(String itemName: statements.keySet()) {
                transaction.insert(
                        Insert.newBuilder()
                                .namespace("order")
                                .table("statements")
                                .partitionKey(Key.ofText("statement_id", UUID.randomUUID().toString()))
                                .textValue("order_id", orderId)
                                .textValue("customer_id", customerId)
                                .textValue("item_name", itemName)
                                .intValue("count", statements.get(itemName))
                                .build()
                );
            }
            transaction.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if(transaction != null) {
                transaction.abort();
            }
            return false;
        }
    }
}
