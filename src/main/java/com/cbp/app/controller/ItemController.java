package com.cbp.app.controller;

import com.cbp.app.model.response.ItemResponse;
import com.cbp.app.model.response.ItemsResponse;
import com.cbp.app.repository.ItemRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

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
}
