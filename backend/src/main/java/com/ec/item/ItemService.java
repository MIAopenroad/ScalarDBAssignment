package com.ec.item;

import com.scalar.db.exception.transaction.AbortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {
    private final ItemRepository repository;

    @Autowired
    public ItemService(ItemRepository repository) {
        this.repository = repository;
    }

    public List<Item> getAllItems() throws AbortException {
        return repository.getAllItems();
    }
    public boolean addItem(Item item) throws AbortException {
        return repository.addItem(item);
    }
}
