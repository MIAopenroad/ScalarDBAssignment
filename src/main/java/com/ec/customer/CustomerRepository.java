package com.ec.customer;

import com.scalar.db.api.*;
import com.scalar.db.exception.transaction.AbortException;
import com.scalar.db.io.Key;
import com.scalar.db.service.TransactionFactory;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CustomerRepository {
    private final DistributedTransactionManager manager;
    public CustomerRepository() throws IOException {
        TransactionFactory factory = TransactionFactory.create("database.properties");
        this.manager = factory.getTransactionManager();
    }

    public boolean signin(final String email, final String password) throws AbortException {
        DistributedTransaction transaction = null;
        try {
            transaction = this.manager.start();
            Optional<Result> user = transaction.get(Get.newBuilder()
                    .namespace("customer")
                    .table("customers")
                    .partitionKey(Key.ofText("email", email)).build()
            );

            if(!user.isPresent()) {
                transaction.abort();
                return false;
            }
            //validate password
            //TODO: hash password with jwt.
            String hashedPassword = user.get().getText("password");
            boolean ok = validatePassword(password, hashedPassword);
            if(!ok) {
                transaction.abort();
                return false;
            }
            transaction.commit();
            return true;
        } catch (Exception e) {
            if(transaction != null) {
                transaction.abort();
            }
            e.printStackTrace();
            return false;
        }
    }
    private boolean validatePassword(String password, String hashedPassword) {
        //TODO: implement hash password
        return password.equals(hashedPassword);
    }

    public boolean signup(String email, String password) throws AbortException {
        DistributedTransaction transaction = null;
        try {
            transaction = this.manager.start();
            Optional<Result> customer = transaction.get(Get.newBuilder()
                    .namespace("customer")
                    .table("customers")
                    .partitionKey(Key.ofText("email", email))
                    .build()
            );
            //もうすでに会員登録済み
            if(customer.isPresent()) {
                transaction.abort();
                return false;
            }
            //登録してないなら会員登録する
            String hashedPassword = hashPassword(password);
            transaction.put(
                    Put.newBuilder()
                    .namespace("customer")
                    .table("customers")
                    .partitionKey(Key.ofText("email", email))
                            .textValue("id", UUID.randomUUID().toString())
                            .textValue("password", hashedPassword).build()
            );
            transaction.commit();
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            if(transaction != null) {
                transaction.abort();
            }
            return false;
        }
    }

    private String hashPassword(String password) {
        //TODO: implement hash password
        return password;
    }

    public List<Customer> getAllCustomers() throws AbortException {
        DistributedTransaction transaction = null;
        try {
            transaction = this.manager.start();
            List<Result> res = transaction.scan(
                    Scan.newBuilder()
                            .namespace("customer")
                            .table("customers")
                            .all()
                            .projections("id", "email", "password")
                            .build()
            );
            List<Customer> customers = new ArrayList<>();
            for(Result customer : res) {
                String id = customer.getText("id");
                String email = customer.getText("email");
                String password = customer.getText("password");
                customers.add(new Customer(id, email, password));
            }
            transaction.commit();
            return customers;
        } catch (Exception e) {
            e.printStackTrace();
            if(transaction != null) {
                transaction.abort();
            }
            return new ArrayList<>();
        }
    }
}
