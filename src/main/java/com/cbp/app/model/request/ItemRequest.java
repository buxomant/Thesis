package com.cbp.app.model.request;

import com.cbp.app.model.db.Item;

public class ItemRequest {
    private int itemId;
    private String name;
    private String url;

    public ItemRequest() { }

    public ItemRequest(Item item) {
        this.itemId = item.getItemId();
        this.name = item.getName();
        this.url = item.getUrl();
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
