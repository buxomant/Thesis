package com.cbp.app.controller;

import com.cbp.app.model.db.Item;
import com.cbp.app.model.response.ItemPriceResponse;
import com.cbp.app.model.response.ItemPricesResponse;
import com.cbp.app.repository.ItemPriceRepository;
import com.cbp.app.repository.ItemRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class ItemPricesController {

    private final ItemPriceRepository itemPriceRepository;
    private final ItemRepository itemRepository;

    public ItemPricesController(
        ItemPriceRepository itemPriceRepository,
        ItemRepository itemRepository
    ) {
        this.itemPriceRepository = itemPriceRepository;
        this.itemRepository = itemRepository;
    }

    @RequestMapping(value = "/item-prices/{itemId}", method = RequestMethod.GET)
    public ItemPricesResponse getItemPrices(@PathVariable int itemId) {
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isPresent()) {
            List<ItemPriceResponse> itemPrices = itemPriceRepository
                .findAllByItem(item.get())
                .stream()
                .map(ItemPriceResponse::new)
                .collect(Collectors.toList());
            return new ItemPricesResponse(itemPrices);
        } else {
            return new ItemPricesResponse(Collections.emptyList());
        }
    }
}
