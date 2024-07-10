package com.ec.item;

import com.scalar.db.exception.transaction.AbortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {
    private final ItemService service;
    @Autowired
    public ItemController(ItemService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public List<Item> getAllItems() throws AbortException {
        return service.getAllItems();
    }
    @PostMapping  ("/add")
    public boolean addItem(@RequestBody Item item) throws AbortException {
        return service.addItem(item);
    }
}
