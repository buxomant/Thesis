package com.cbp.app.service;

import com.cbp.app.model.db.Item;
import com.cbp.app.model.db.ItemPrice;
import com.cbp.app.repository.ItemPriceRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ItemPriceService {

    private final ItemPriceRepository itemPriceRepository;

    public ItemPriceService(ItemPriceRepository itemPriceRepository) {
        this.itemPriceRepository = itemPriceRepository;
    }

    public void checkNewItemPrice(Item item) throws IOException {
//        Document productPage = Jsoup.connect(item.getUrl()).get();
//        Elements priceElements = productPage.select(".product-page-pricing").select(".product-new-price");
//        Element priceElement = priceElements.get(0);
//        String wholePrice = priceElement.textNodes().stream()
//            .filter(node -> !node.text().trim().equals(""))
//            .findFirst()
//            .orElse(new TextNode(""))
//            .text()
//            .replace(".", "");
//        String fractionalPrice = priceElement.select("sup").text();
//        String fullPrice = wholePrice + '.' + fractionalPrice;
        Double price = (500 + Math.random() * (1000 - 500));
        String fullPrice = price.toString();

        ItemPrice itemPrice = new ItemPrice(item.getItemId(), Float.parseFloat(fullPrice));
        itemPriceRepository.save(itemPrice);
    }
}
