package com.cbp.app.model.db;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class ItemPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int itemPriceId;

    @Column
    @NotNull
    private int itemId;

    @Column
    @NotNull
    private Float price;

    @Column
    private LocalDateTime timeChecked = LocalDateTime.now();

    public ItemPrice() { }

    public ItemPrice(@NotNull int itemId, @NotNull Float price) {
        this.itemId = itemId;
        this.price = price;
    }

    public int getItemPriceId() {
        return itemPriceId;
    }

    public void setItemPriceId(int itemPriceId) {
        this.itemPriceId = itemPriceId;
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
