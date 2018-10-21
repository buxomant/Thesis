package com.cbp.app.model.response;

import java.util.List;

public class ItemPricesResponse {
    private List<ItemPriceResponse> itemPrices;

    public ItemPricesResponse() { }

    public ItemPricesResponse(List<ItemPriceResponse> itemPrices) {
        this.itemPrices = itemPrices;
    }

    public List<ItemPriceResponse> getItemPrices() {
        return itemPrices;
    }

    public void setItemPrices(List<ItemPriceResponse> itemPrices) {
        this.itemPrices = itemPrices;
    }
}
