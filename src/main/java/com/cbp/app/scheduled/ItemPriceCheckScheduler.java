package com.cbp.app.scheduled;

import com.cbp.app.model.db.ItemPriceHistory;
import com.cbp.app.repository.ItemPriceHistoryRepository;
import com.cbp.app.repository.ItemRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ItemPriceCheckScheduler {

    private final ItemPriceHistoryRepository itemPriceHistoryRepository;
    private final ItemRepository itemRepository;

    public ItemPriceCheckScheduler(
        ItemPriceHistoryRepository itemPriceHistoryRepository,
        ItemRepository itemRepository
    ) {
        this.itemPriceHistoryRepository = itemPriceHistoryRepository;
        this.itemRepository = itemRepository;
    }

    private static final int ONE_HOUR_IN_MILLISECONDS = 60 * 60 * 1000;

    @Scheduled(fixedRate = ONE_HOUR_IN_MILLISECONDS)
    public void checkItemPrices() {
        itemRepository.findAll().forEach(item -> {
            try {
                Document productPage = Jsoup.connect(item.getUrl()).get();
                Elements priceElements = productPage.select(".product-page-pricing").select(".product-new-price");
                Element priceElement = priceElements.get(0);
                String wholePrice = priceElement.textNodes().stream()
                    .filter(node -> !node.text().trim().equals(""))
                    .findFirst()
                    .orElse(new TextNode(""))
                    .text();
                String fractionalPrice = priceElement.select("sup").text();
                String fullPrice = wholePrice + '.' + fractionalPrice;

                ItemPriceHistory itemPriceHistory = new ItemPriceHistory(item, Float.parseFloat(fullPrice));
                itemPriceHistoryRepository.save(itemPriceHistory);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
