package com.cbp.app.model.response;

import com.cbp.app.model.db.ItemPrice;

import java.time.LocalDateTime;

public class ItemPriceResponse {
    private int itemId;
    private Float price;
    private LocalDateTime timeChecked;

    public ItemPriceResponse() { }

    public ItemPriceResponse(ItemPrice itemPrice) {
        this.itemId = itemPrice.getItem().getItemId();
        this.price = itemPrice.getPrice();
        this.timeChecked = itemPrice.getTimeChecked();
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public LocalDateTime getTimeChecked() {
        return timeChecked;
    }

    public void setTimeChecked(LocalDateTime timeChecked) {
        this.timeChecked = timeChecked;
    }
}
