package com.cbp.app.scheduled;

import com.cbp.app.model.db.Item;
import com.cbp.app.model.db.ItemPrice;
import com.cbp.app.repository.ItemPriceRepository;
import com.cbp.app.repository.ItemRepository;
import com.cbp.app.service.ItemPriceService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class ItemPriceCheckScheduler {

    private final ItemRepository itemRepository;
    private final ItemPriceRepository itemPriceRepository;
    private final ItemPriceService itemPriceService;
    private final boolean jobEnabled;

    public ItemPriceCheckScheduler(
        ItemRepository itemRepository,
        ItemPriceRepository itemPriceRepository,
        ItemPriceService itemPriceService,
        @Value("${item-price-check-scheduler.enabled}") boolean jobEnabled
    ) {
        this.itemRepository = itemRepository;
        this.itemPriceRepository = itemPriceRepository;
        this.itemPriceService = itemPriceService;
        this.jobEnabled = jobEnabled;
    }

    private static final int ONE_MINUTE_IN_MILLISECONDS = 60 * 1000;
    private static final int PRICE_CHECK_THRESHOLD_IN_HOURS = 1;

//    @Scheduled(fixedRate = ONE_MINUTE_IN_MILLISECONDS)
    public void checkItemPrices() {
        if (!jobEnabled) {
            return;
        }

        List<Item> items = itemRepository.findAllItemsThatNeedPriceCheckingInOrder();
        if (items.size() > 0) {
            Item item = items.get(0);
            Optional<ItemPrice> itemPrice = itemPriceRepository.findFirstByItemIdOrderByTimeCheckedDesc(item.getItemId());
            if (!itemPrice.isPresent() || itemNeedsNewCheck(itemPrice.get())) {
                try {
                    itemPriceService.checkNewItemPrice(item);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private boolean itemNeedsNewCheck(ItemPrice itemPrice) {
        return itemPrice.getTimeChecked().isBefore(LocalDateTime.now().minusHours(PRICE_CHECK_THRESHOLD_IN_HOURS));
    }
}
