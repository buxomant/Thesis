package com.cbp.app.scheduled;

import com.cbp.app.repository.ItemRepository;
import com.cbp.app.service.ItemPriceService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ItemPriceCheckScheduler {

    private final ItemRepository itemRepository;
    private final ItemPriceService itemPriceService;

    public ItemPriceCheckScheduler(
        ItemRepository itemRepository,
        ItemPriceService itemPriceService
    ) {
        this.itemRepository = itemRepository;
        this.itemPriceService = itemPriceService;
    }

    private static final int ONE_HOUR_IN_MILLISECONDS = 60 * 60 * 1000;

    @Scheduled(fixedRate = ONE_HOUR_IN_MILLISECONDS)
    public void checkItemPrices() {
        itemRepository.findAll().forEach(item -> {
            try {
                itemPriceService.checkNewItemPrice(item);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
