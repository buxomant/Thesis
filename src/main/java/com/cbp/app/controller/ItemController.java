package com.cbp.app.controller;

import com.cbp.app.model.db.Item;
import com.cbp.app.model.request.ItemRequest;
import com.cbp.app.model.response.ItemResponse;
import com.cbp.app.model.response.ItemsResponse;
import com.cbp.app.repository.ItemRepository;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
public class ItemController {

    private final ItemRepository itemRepository;

    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @RequestMapping(value = "/items", method = RequestMethod.GET)
    public ItemsResponse getItems() {
        List<ItemResponse> items = itemRepository.findAll()
            .stream()
            .map(ItemResponse::new)
            .collect(Collectors.toList());
        return new ItemsResponse(items);
    }

    @RequestMapping(value = "/item", method = RequestMethod.POST)
    public ItemResponse createItem(
        @Valid @RequestBody ItemRequest request
    ) {
        Item newItem = new Item(request.getName(), request.getUrl());
        Item savedItem = itemRepository.save(newItem);
        return new ItemResponse(savedItem);
    }
}
