package com.ec.customer;

import com.scalar.db.api.*;
import com.scalar.db.exception.transaction.AbortException;
import com.scalar.db.io.Key;
import com.scalar.db.service.TransactionFactory;
import org.springframework.stereotype.Repository;

import javax.crypto.*;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Repository
public class CustomerRepository {
    private final DistributedTransactionManager manager;
    private final int keySize = 256;
    private SecretKey secretKey;
    public CustomerRepository() throws IOException, NoSuchAlgorithmException {
        TransactionFactory factory = TransactionFactory.create("database.properties");
        this.manager = factory.getTransactionManager();
        this.secretKey = this.generateAESKey();
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
            transaction.insert(
                    Insert.newBuilder()
                    .namespace("customer")
                    .table("customers")
                    .partitionKey(Key.ofText("email", email))
                            .textValue("customer_id", UUID.randomUUID().toString())
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

    private String hashPassword(String password) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(password.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
    private boolean validatePassword(String password, String hashedPassword) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(hashedPassword));
        return (new String(decryptedBytes)).equals(password);
    }

    private SecretKey generateAESKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(this.keySize);
        return keyGenerator.generateKey();
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
                            .projections("customer_id", "email", "password")
                            .build()
            );
            List<Customer> customers = new ArrayList<>();
            for(Result customer : res) {
                String customerId = customer.getText("customer_id");
                String email = customer.getText("email");
                String password = customer.getText("password");
                customers.add(new Customer(customerId, email, password));
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
