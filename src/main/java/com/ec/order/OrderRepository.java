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
    // データベースにある全ての注文の履歴を取得する, 確認用のAPI, Java側で頑張ればやりたいことはできるけど本当にそれでいいの？
    // TwoPhaseのやつ使ってJOINすればいいか
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
                                .partitionKey(Key.ofText("order_id", order.getOrderId()))
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
            if(transaction != null) {
                transaction.abort();
            }
            return new ArrayList<>();
        }
    }

    //ユーザごとの注文履歴
    //入力: customerId
    //処理: customerIdでList<OrderId>を取得する
    //orderIdを回して何を何個買ったかと成功したか、失敗したかを取得する。
    //これもJava側で頑張ればいけるけど、本当にそれでいい？
    //upsertしかないのが問題だよなぁ...
    public List<Order> getOrdersByCustomerID(String customerId) {
        return null;
    }

    //注文をデータベースに登録する
    //入力: 誰が何を何個注文したかがListでくる
    //処理1. 誰が何時何分に注文したかのregisterしてorderIdを生成
    //処理2. 処理1が成功したら、そのorderIdで一つの商品とそれを何個買ったかをstatus=trueで登録する、失敗したらstatusがfalse
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
                return false;
            }
            String customerId = res.get().getText("id");
            String orderId = UUID.randomUUID().toString();
            //2. orderId, customerId, timestamp, statusからordersに登録
            System.out.println("customer_id: " + customerId);
            System.out.println("order_id: " + orderId);
            transaction.put(
                    Put.newBuilder()
                            .namespace("order")
                            .table("orders")
                            .partitionKey(Key.ofText("order_id", customerId))
                            .textValue("customer_id", customerId)
                            .textValue("timestamp", new Date().toString())
                            .booleanValue("status", true)
                            .build()
            );
            //3. orderId, statementId, statement.itemName, statement.countからstatementsに登録
            for(String itemName: statements.keySet()) {
                transaction.put(
                        Put.newBuilder()
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
