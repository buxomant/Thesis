package com.cbp.app.model.db;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class ItemPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int itemPriceId;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "item_id")
    private Item item;

    @Column
    @NotNull
    private Float price;

    @Column
    private LocalDateTime timeChecked = LocalDateTime.now();

    public ItemPrice() { }

    public ItemPrice(@NotNull Item item, @NotNull Float price) {
        this.item = item;
        this.price = price;
    }

    public int getItemPriceId() {
        return itemPriceId;
    }

    public void setItemPriceId(int itemPriceId) {
        this.itemPriceId = itemPriceId;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
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
