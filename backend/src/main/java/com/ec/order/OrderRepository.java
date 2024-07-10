package com.ec.order;


import com.ec.item.Item;
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
                                .projections("statement_id", "order_id", "item_id", "count")
                                .build()
                );
                List<Statements> statements = new ArrayList<>();
                for(Result row : rows) {
                    String statementId = row.getText("statement_id");
                    String orderId = row.getText("order_id");
                    String itemId = row.getText("item_id");
                    int count = row.getInt("count");
                    System.out.println("itemId: " + itemId);
                    System.out.println("count: " + count);
                    System.out.println();
                    Statements statement = new Statements(statementId, orderId, itemId, count);
                    statements.add(statement);
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
    public List<OrderWithExtendedStatements> getOrdersByEmail(String email) throws AbortException {
        //1. emailからcustomerIdを取得
        DistributedTransaction transaction = null;
        try {
            transaction = this.manager.start();
            Optional<Result> res = transaction.get(
                    Get.newBuilder()
                            .namespace("customer")
                            .table("customers")
                            .partitionKey(Key.ofText("email", email))
                            .projections("customer_id", "email", "password")
                            .build()
            );
            if(!res.isPresent()) {
                transaction.abort();
                return new ArrayList<>();
            }
            String customerId = res.get().getText("customer_id");
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
            List<OrderWithExtendedStatements> resp = new ArrayList<>();
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
                                .projections("statement_id", "order_id", "item_id", "count")
                                .build()
                );
                List<ExtendedStatement> extStatements = new ArrayList<>();
                for(Result row: statementsByOrderId) {
                    Optional<Result> item_ = transaction.get(
                            Get.newBuilder()
                                    .namespace("item")
                                    .table("item_info")
                                    .partitionKey(Key.ofText("item_id", row.getText("item_id")))
                                    .projections("item_id", "name", "price", "stock", "description", "image_url")
                                    .build()
                    );
                    if(item_.isPresent()){
                        String itemId = item_.get().getText("item_id");
                        String name = item_.get().getText("name");
                        Double price = item_.get().getDouble("price");
                        int stock = item_.get().getInt("stock");
                        String description = item_.get().getText("description");
                        String imageUrl = item_.get().getText("image_url");
                        ExtendedStatement extStatement = new ExtendedStatement(
                                row.getText("statement_id"), row.getText("order_id"),
                                row.getText("item_id"), row.getInt("count"),
                                new Item(itemId, name,  price, stock, description, imageUrl)
                        );
                        extStatements.add(extStatement);
                    } else {
                        return new ArrayList<>();
                    }
                }
                resp.add(new OrderWithExtendedStatements(orderId, customerId, timestamp, status, extStatements));
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
        DistributedTransaction transaction = null;
        try {
            transaction = this.manager.start();
            // 1. emailからcustomerIdを取得
            Optional<Result> res = transaction.get(
                    Get.newBuilder()
                            .namespace("customer")
                            .table("customers")
                            .partitionKey(Key.ofText("email", email))
                            .projections("customer_id", "email", "password")
                            .build()
            );
            if(!res.isPresent()) {
                transaction.abort();
                return false;
            }
            String customerId = res.get().getText("customer_id");
            // 2. 注文商品の在庫数を確認
            boolean status = true;
            List<Result> items = new ArrayList<>();
            for(String itemId: statements.keySet()) {
                Optional<Result> item = transaction.get(
                        Get.newBuilder()
                                .namespace("item")
                                .table("item_info")
                                .partitionKey(Key.ofText("item_id", itemId))
                                .where(ConditionBuilder.column("stock").isGreaterThanOrEqualToInt(statements.get(itemId)))
                                .projections("item_id", "stock")
                                .build()
                );
                if(item.isPresent()) {
                    items.add(item.get());
                } else {
                    status = false;
                }
            }
            if(status) {
                for(Result item: items) {
                    String itemId = item.getText("item_id");
                    transaction.update(
                            Update.newBuilder()
                                    .namespace("item")
                                    .table("item_info")
                                    .partitionKey(Key.ofText("item_id", itemId))
                                    .intValue("stock", item.getInt("stock") - statements.get(itemId))
                                    .build()
                    );
                }
            }
            // 3. 注文情報を登録
            String orderId = UUID.randomUUID().toString();
            transaction.insert(
                    Insert.newBuilder()
                            .namespace("order")
                            .table("orders")
                            .partitionKey(Key.ofText("order_id", orderId))
                            .textValue("customer_id", customerId)
                            .textValue("timestamp", new Date().toString())
                            .booleanValue("status", status)
                            .build()
            );
            for(String itemId: statements.keySet()) {
                transaction.insert(
                        Insert.newBuilder()
                                .namespace("order")
                                .table("statements")
                                .partitionKey(Key.ofText("statement_id", UUID.randomUUID().toString()))
                                .textValue("order_id", orderId)
                                .textValue("item_id", itemId)
                                .intValue("count", statements.get(itemId))
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
