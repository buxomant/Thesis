package com.cbp.app.model.response;

import java.util.List;

public class ItemsResponse {
    private List<ItemResponse> items;

    public ItemsResponse() {}

    public ItemsResponse(List<ItemResponse> items) {
        this.items = items;
    }

    public List<ItemResponse> getItems() {
        return items;
    }

    public void setItems(List<ItemResponse> items) {
        this.items = items;
    }
}
