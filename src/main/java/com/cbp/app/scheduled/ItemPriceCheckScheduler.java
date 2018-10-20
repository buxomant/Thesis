package com.cbp.app.scheduled;

import com.cbp.app.repository.ItemPriceHistoryRepository;
import org.springframework.stereotype.Component;

@Component
public class ItemPriceCheckScheduler {

    private final ItemPriceHistoryRepository itemPriceHistoryRepository;

    public ItemPriceCheckScheduler(ItemPriceHistoryRepository itemPriceHistoryRepository) {
        this.itemPriceHistoryRepository = itemPriceHistoryRepository;
    }
}
